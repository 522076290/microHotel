package com.ruoyi.hotel.mapper;

import java.util.List;
import com.ruoyi.hotel.domain.HotelInvoices;

/**
 * 酒店账单Mapper接口
 * 
 * @author lan
 * @date 2024-12-24
 */
public interface HotelInvoicesMapper 
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
    public List<HotelInvoices> selectHotelInvoicesList(HotelInvoices hotelInvoices);

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
     * 删除酒店账单
     * 
     * @param id 酒店账单主键
     * @return 结果
     */
    public int deleteHotelInvoicesById(Long id);

    /**
     * 批量删除酒店账单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHotelInvoicesByIds(Long[] ids);
}
