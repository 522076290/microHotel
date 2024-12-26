package com.ruoyi.hotel.service.impl;

import java.util.List;
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

    /**
     * 查询酒店订单
     * 
     * @param id 酒店订单主键
     * @return 酒店订单
     */
    @Override
    public HotelOrders selectHotelOrdersById(Long id)
    {
        return hotelOrdersMapper.selectHotelOrdersById(id);
    }

    /**
     * 查询酒店订单列表
     * 
     * @param hotelOrders 酒店订单
     * @return 酒店订单
     */
    @Override
    public List<HotelOrders> selectHotelOrdersList(HotelOrders hotelOrders)
    {
        return hotelOrdersMapper.selectHotelOrdersList(hotelOrders);
    }

    /**
     * 新增酒店订单
     * 
     * @param hotelOrders 酒店订单
     * @return 结果
     */
    @Override
    public int insertHotelOrders(HotelOrders hotelOrders)
    {
        return hotelOrdersMapper.insertHotelOrders(hotelOrders);
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
}
