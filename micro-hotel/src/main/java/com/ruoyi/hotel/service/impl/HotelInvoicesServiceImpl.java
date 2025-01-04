package com.ruoyi.hotel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.hotel.domain.resp.HotelInvoicesResp;
import com.ruoyi.hotel.mapper.HotelOrdersMapper;
import com.ruoyi.hotel.mapper.HotelRoomsMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.hotel.mapper.HotelInvoicesMapper;
import com.ruoyi.hotel.domain.HotelInvoices;
import com.ruoyi.hotel.service.IHotelInvoicesService;


/**
 * 酒店账单Service业务层处理
 * 
 * @author lan
 * @date 2024-12-24
 */
@Service
public class HotelInvoicesServiceImpl implements IHotelInvoicesService 
{
    @Autowired
    private HotelInvoicesMapper hotelInvoicesMapper;

    @Autowired
    private HotelOrdersMapper hotelOrdersMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedissonClient redissonClient; // 注入 RedissonClient

    // 账单队列
    private static final String INVOICES_DELAY_QUEUE = "INVOICES_DELAY_QUEUE";

    /**
     * 查询酒店账单
     * 
     * @param id 酒店账单主键
     * @return 酒店账单
     */
    @Override
    public HotelInvoices selectHotelInvoicesById(Long id)
    {
        return hotelInvoicesMapper.selectHotelInvoicesById(id);
    }

    /**
     * 查询酒店账单列表
     * 
     * @param hotelInvoices 酒店账单
     * @return 酒店账单
     */
    @Override
    public List<HotelInvoicesResp> selectHotelInvoicesList(HotelInvoices hotelInvoices)
    {
        List <HotelInvoices> list = hotelInvoicesMapper.selectHotelInvoicesList(hotelInvoices);
        List<HotelInvoicesResp> hotelInvoicesRespList = new ArrayList<>();
        for (HotelInvoices hotelInvoices1 : list) {
            HotelInvoicesResp hotelInvoicesResp = new HotelInvoicesResp();
            BeanUtils.copyBeanProp(hotelInvoicesResp, hotelInvoices1);
            // 查询用户的信息添加进hotelOrdersResp
            hotelInvoicesResp.setSysUser(sysUserMapper.selectUserById(hotelInvoices1.getCustomerId()));
            // 获取房间信息添加进hotelOrdersResp
            hotelInvoicesResp.setOrders(hotelOrdersMapper.selectHotelOrdersById(hotelInvoices1.getOrderId()));
            hotelInvoicesRespList.add(hotelInvoicesResp);
        }

        return hotelInvoicesRespList;
    }

    /**
     * 新增酒店账单
     * 
     * @param hotelInvoices 酒店账单
     * @return 结果
     */
    @Override
    public int insertHotelInvoices(HotelInvoices hotelInvoices)
    {
        hotelInvoicesMapper.insertHotelInvoices(hotelInvoices);
        Long invoicesId = hotelInvoices.getId(); // 假设数据库自增ID

        // 1. 判断是否需要延迟队列
        if (hotelInvoices.getPaymentStatus() == 1){
            // 2. 使用 Redisson 的延时队列设置 10 分钟过期
            RQueue<Long> queue = redissonClient.getQueue(INVOICES_DELAY_QUEUE);
            RDelayedQueue<Long> delayedQueue = redissonClient.getDelayedQueue(queue);

            // 把账单ID放入队列，延时10分钟
            delayedQueue.offer(invoicesId, 1, TimeUnit.MINUTES);
        }

        return invoicesId.intValue();
    }

    /**
     * 修改酒店账单
     * 
     * @param hotelInvoices 酒店账单
     * @return 结果
     */
    @Override
    public int updateHotelInvoices(HotelInvoices hotelInvoices)
    {
        return hotelInvoicesMapper.updateHotelInvoices(hotelInvoices);
    }

    /**
     * 批量删除酒店账单
     * 
     * @param ids 需要删除的酒店账单主键
     * @return 结果
     */
    @Override
    public int deleteHotelInvoicesByIds(Long[] ids)
    {
        return hotelInvoicesMapper.deleteHotelInvoicesByIds(ids);
    }

    /**
     * 删除酒店账单信息
     * 
     * @param id 酒店账单主键
     * @return 结果
     */
    @Override
    public int deleteHotelInvoicesById(Long id)
    {
        return hotelInvoicesMapper.deleteHotelInvoicesById(id);
    }
}
