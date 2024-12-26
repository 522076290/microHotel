package com.ruoyi.hotel.mapper;

import java.util.List;
import com.ruoyi.hotel.domain.HotelRooms;

/**
 * 酒店房间Mapper接口
 * 
 * @author Lan
 * @date 2024-12-25
 */
public interface HotelRoomsMapper 
{
    /**
     * 查询酒店房间
     * 
     * @param id 酒店房间主键
     * @return 酒店房间
     */
    public HotelRooms selectHotelRoomsById(Long id);

    /**
     * 查询酒店房间列表
     * 
     * @param hotelRooms 酒店房间
     * @return 酒店房间集合
     */
    public List<HotelRooms> selectHotelRoomsList(HotelRooms hotelRooms);

    /**
     * 新增酒店房间
     * 
     * @param hotelRooms 酒店房间
     * @return 结果
     */
    public int insertHotelRooms(HotelRooms hotelRooms);

    /**
     * 修改酒店房间
     * 
     * @param hotelRooms 酒店房间
     * @return 结果
     */
    public int updateHotelRooms(HotelRooms hotelRooms);

    /**
     * 删除酒店房间
     * 
     * @param id 酒店房间主键
     * @return 结果
     */
    public int deleteHotelRoomsById(Long id);

    /**
     * 批量删除酒店房间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHotelRoomsByIds(Long[] ids);
}
