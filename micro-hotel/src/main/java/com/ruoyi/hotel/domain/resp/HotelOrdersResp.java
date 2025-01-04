package com.ruoyi.hotel.domain.resp;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.domain.HotelRooms;
import lombok.Data;

/**
 * @author lan
 * @version 1.0
 * @data 1/3/2025 11:36 上午
 */
@Data
public class HotelOrdersResp extends HotelOrders {

    /**
     * 房间信息
     */
    private HotelRooms hotelRooms;

    /**
     * 用户信息
     */
    private SysUser sysUser;
}
