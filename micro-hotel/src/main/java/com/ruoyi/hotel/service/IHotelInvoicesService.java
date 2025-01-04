package com.ruoyi.hotel.service;

import java.util.List;
import com.ruoyi.hotel.domain.HotelInvoices;
import com.ruoyi.hotel.domain.resp.HotelInvoicesResp;

/**
 * 酒店账单Service接口
 * 
 * @author lan
 * @date 2024-12-24
 */
public interface IHotelInvoicesService 
{
    /**
     * 查询酒店账单
     * 
     * @param id 酒店账单主键
     * @return 酒店账单
     */
    public HotelInvoices selectHotelInvoicesById(Long id);

    /**
     * 查询酒店账单列表
     * 
     * @param hotelInvoices 酒店账单
     * @return 酒店账单集合
     */
    public List<HotelInvoicesResp> selectHotelInvoicesList(HotelInvoices hotelInvoices);

    /**
     * 新增酒店账单
     * 
     * @param hotelInvoices 酒店账单
     * @return 结果
     */
    public int insertHotelInvoices(HotelInvoices hotelInvoices);

    /**
     * 修改酒店账单
     * 
     * @param hotelInvoices 酒店账单
     * @return 结果
     */
    public int updateHotelInvoices(HotelInvoices hotelInvoices);

    /**
     * 批量删除酒店账单
     * 
     * @param ids 需要删除的酒店账单主键集合
     * @return 结果
     */
    public int deleteHotelInvoicesByIds(Long[] ids);

    /**
     * 删除酒店账单信息
     * 
     * @param id 酒店账单主键
     * @return 结果
     */
    public int deleteHotelInvoicesById(Long id);
}
