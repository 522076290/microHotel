package com.ruoyi.hotel.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.hotel.domain.resp.HotelOrdersResp;
import com.ruoyi.hotel.mapper.HotelRoomsMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.ruoyi.hotel.mapper.HotelOrdersMapper;
import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.service.IHotelOrdersService;

/**
 * 酒店订单Service业务层处理
 * 
 * @author Lan
 * @date 2024-12-24
 */
@Service
public class HotelOrdersServiceImpl implements IHotelOrdersService 
{
    @Autowired
    private HotelOrdersMapper hotelOrdersMapper;

    @Autowired
    private HotelRoomsMapper hotelRoomsMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedissonClient redissonClient; // 注入 RedissonClient


    // 订单编号生成器
    private static final String DATE_FORMAT = "yyyyMMdd";
    // 订单编号长度
    private static final int ORDER_NUMBER_LENGTH = 6;
    // 订单队列
    private static final String ORDER_DELAY_QUEUE = "ORDER_DELAY_QUEUE";

    /**
     * 查询酒店订单
     * 
     * @param id 酒店订单主键
     * @return 酒店订单
     */
    @Override
    public HotelOrdersResp selectHotelOrdersById(Long id)
    {
        HotelOrders hotelOrder = hotelOrdersMapper.selectHotelOrdersById(id);
        HotelOrdersResp hotelOrdersResp = new HotelOrdersResp();
        // 通过反射将hotelOrder值赋值hotelOrdersResp
        BeanUtils.copyProperties(hotelOrder, hotelOrdersResp);
        // 查询用户的信息添加进hotelOrdersResp
        hotelOrdersResp.setSysUser(sysUserMapper.selectUserById(hotelOrder.getCustomerId()));
        // 获取房间信息添加进hotelOrdersResp
        hotelOrdersResp.setHotelRooms(hotelRoomsMapper.selectHotelRoomsById(hotelOrder.getRoomId()));

        return hotelOrdersResp;
    }

    /**
     * 查询酒店订单列表
     * 
     * @param hotelOrders 酒店订单
     * @return 酒店订单
     */
    @Override
    public List<HotelOrdersResp> selectHotelOrdersList(HotelOrders hotelOrders)
    {
        List<HotelOrders> hotelOrderList = hotelOrdersMapper.selectHotelOrdersList(hotelOrders);
        List<HotelOrdersResp> hotelOrdersRespList = new ArrayList<>();
        for (HotelOrders hotelOrder : hotelOrderList) {
            HotelOrdersResp hotelOrdersResp = new HotelOrdersResp();
            // 通过反射将hotelOrder值赋值hotelOrdersResp
            BeanUtils.copyProperties(hotelOrder, hotelOrdersResp);
            // 查询用户的信息添加进hotelOrdersResp
            hotelOrdersResp.setSysUser(sysUserMapper.selectUserById(hotelOrder.getCustomerId()));
            // 获取房间信息添加进hotelOrdersResp
            hotelOrdersResp.setHotelRooms(hotelRoomsMapper.selectHotelRoomsById(hotelOrder.getRoomId()));
            hotelOrdersRespList.add(hotelOrdersResp);
        }

        return hotelOrdersRespList;
    }

    /**
     * 新增酒店订单
     * 
     * @param hotelOrders 酒店订单
     * @return 结果
     */
    @Override
    public HotelOrders insertHotelOrders(HotelOrders hotelOrders)
    {
        // 先检查预定时间内房间否被锁定
        // 1. 检查时间段是否已被预定
        int overlapCount = hotelOrdersMapper.countOverlapOrders(hotelOrders);
        if (overlapCount > 0) {
            // 已经有人预定
            throw new RuntimeException("该时间段已被预订，请选择其他时间。");
        }

        hotelOrders.setOrderNumber(generateOrderNumber());
        // 根据入住日期和离店日期计算天数
        long betweenDay = DateUtil.between(hotelOrders.getStartDate(), hotelOrders.getEndDate(), DateUnit.DAY);
        // 计算总价
        BigDecimal totalPrice = hotelRoomsMapper.selectHotelRoomsById(hotelOrders.getRoomId()).getPrice().multiply(new BigDecimal(betweenDay));
        hotelOrders.setTotalPrice(totalPrice);
        // 设置状态
        hotelOrders.setStatus(0L);
        hotelOrders.setOrderDate(new Date());
        hotelOrders.setMerchantAcceptStatus(0L);
        hotelOrdersMapper.insertHotelOrders(hotelOrders);

        // 2. 使用 Redisson 的延时队列设置 10 分钟过期
        Long orderId = hotelOrders.getId(); // 假设数据库自增ID
        RQueue<Long> queue = redissonClient.getQueue(ORDER_DELAY_QUEUE);
        RDelayedQueue<Long> delayedQueue = redissonClient.getDelayedQueue(queue);

        // 把订单ID放入队列，延时10分钟
        delayedQueue.offer(orderId, 1, TimeUnit.MINUTES);

        return hotelOrders;
    }

    /**
     * 修改酒店订单
     * 
     * @param hotelOrders 酒店订单
     * @return 结果
     */
    @Override
    public int updateHotelOrders(HotelOrders hotelOrders)
    {
        return hotelOrdersMapper.updateHotelOrders(hotelOrders);
    }

    /**
     * 批量删除酒店订单
     * 
     * @param ids 需要删除的酒店订单主键
     * @return 结果
     */
    @Override
    public int deleteHotelOrdersByIds(Long[] ids)
    {
        return hotelOrdersMapper.deleteHotelOrdersByIds(ids);
    }

    /**
     * 删除酒店订单信息
     * 
     * @param id 酒店订单主键
     * @return 结果
     */
    @Override
    public int deleteHotelOrdersById(Long id)
    {
        return hotelOrdersMapper.deleteHotelOrdersById(id);
    }

    @Override
    public List<HotelOrders> selecToDaytHotelOrdersList() {
        return hotelOrdersMapper.selectToDayHotelOrdersList();
    }

    @Override
    public List<HotelOrders> selecWeekHotelOrdersList(int weekOffset, HotelOrders hotelOrders) {
        // 计算目标周的开始日期和结束日期
        LocalDate today = LocalDate.now();
        // 确定本周的周一
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        // 计算目标周的周一
        LocalDate targetMonday = thisMonday.plusWeeks(weekOffset);
        // 目标周的周日
        LocalDate targetSunday = targetMonday.plusDays(6);

        // 格式化日期为字符串，假设数据库使用 'yyyy-MM-dd' 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = targetMonday.format(formatter) + " 00:00:00";
        String endDate = targetSunday.format(formatter) + " 23:59:59";

        // 创建参数映射
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("hotelOrders", hotelOrders); // 传递其他查询条件

        return hotelOrdersMapper.selectWeekHotelOrdersList(params);
    }

    public String generateOrderNumber() {
        String orderNumber = "";

        // 获取当前日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = dateFormat.format(new Date());

        // 查询数据库中当天已生成的订单数量
        int currentOrderCount = getOrderCount();

        // 将订单数量加1得到新的订单号的后缀部分
        int newOrderCount = currentOrderCount + 1;
        String orderNumberSuffix = String.format("%0" + ORDER_NUMBER_LENGTH + "d", newOrderCount);

        // 拼接日期和订单号的后缀部分得到最终的订单号
        orderNumber = "DD" +currentDate + orderNumberSuffix;

        return orderNumber;
    }

    private  int getOrderCount() {
        // TODO: 查询数据库中当天已生成的订单数量，并返回
        List<HotelOrders> list =hotelOrdersMapper.selectToDayHotelOrdersList();
        return list.size();
    }

}
