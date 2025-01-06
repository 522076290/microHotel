package com.ruoyi.hotel.service;

import java.util.List;
import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.domain.resp.HotelOrdersResp;

/**
 * 酒店订单Service接口
 * 
 * @author Lan
 * @date 2024-12-24
 */
public interface IHotelOrdersService 
{
    /**
     * 查询酒店订单
     * 
     * @param id 酒店订单主键
     * @return 酒店订单
     */
    public HotelOrdersResp selectHotelOrdersById(Long id);

    /**
     * 查询酒店订单列表
     * 
     * @param hotelOrders 酒店订单
     * @return 酒店订单集合
     */
    public List<HotelOrdersResp> selectHotelOrdersList(HotelOrders hotelOrders);

    /**
     * 新增酒店订单
     * 
     * @param hotelOrders 酒店订单
     * @return 结果
     */
    public HotelOrders insertHotelOrders(HotelOrders hotelOrders);

    /**
     * 修改酒店订单
     * 
     * @param hotelOrders 酒店订单
     * @return 结果
     */
    public int updateHotelOrders(HotelOrders hotelOrders);

    /**
     * 批量删除酒店订单
     * 
     * @param ids 需要删除的酒店订单主键集合
     * @return 结果
     */
    public int deleteHotelOrdersByIds(Long[] ids);

    /**
     * 删除酒店订单信息
     * 
     * @param id 酒店订单主键
     * @return 结果
     */
    public int deleteHotelOrdersById(Long id);

    /**
     * 查询今日订单
     * @return
     */
    public List<HotelOrders> selecToDaytHotelOrdersList();

    /**
     * 查询当周订单
     */
    public List<HotelOrders> selecWeekHotelOrdersList(int weekOffset, HotelOrders hotelOrders);
}
