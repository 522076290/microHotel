package com.ruoyi.hotel.service.impl;

import java.util.List;
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
    public List<HotelInvoices> selectHotelInvoicesList(HotelInvoices hotelInvoices)
    {
        return hotelInvoicesMapper.selectHotelInvoicesList(hotelInvoices);
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
        return hotelInvoicesMapper.insertHotelInvoices(hotelInvoices);
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
