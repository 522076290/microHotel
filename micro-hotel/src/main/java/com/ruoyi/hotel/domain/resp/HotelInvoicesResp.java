package com.ruoyi.hotel.domain.resp;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.hotel.domain.HotelInvoices;
import com.ruoyi.hotel.domain.HotelOrders;
import lombok.Data;


/**
 * @author lan
 * @version 1.0
 * @data 1/4/2025 6:04 下午
 */
@Data
public class HotelInvoicesResp extends HotelInvoices {
    /**
     * 订单信息
     */
    private HotelOrders orders;

    /**
     * 用户信息
     */
    private SysUser sysUser;
}
