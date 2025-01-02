package com.ruoyi.hotel.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hotel.domain.HotelRooms;
import com.ruoyi.hotel.service.IHotelRoomsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店房间Controller
 * 
 * @author Lan
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/hotel/rooms")
public class HotelRoomsController extends BaseController
{
    @Autowired
    private IHotelRoomsService hotelRoomsService;

    /**
     * 查询酒店房间列表
     */
    //  @PreAuthorize("@ss.hasPermi('hotel:rooms:list')")
    @GetMapping("/list")
    public TableDataInfo list(HotelRooms hotelRooms)
    {
        startPage();
        List<HotelRooms> list = hotelRoomsService.selectHotelRoomsList(hotelRooms);
        return getDataTable(list);
    }

    /**
     * 导出酒店房间列表
     */
    @PreAuthorize("@ss.hasPermi('hotel:rooms:export')")
    @Log(title = "酒店房间", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HotelRooms hotelRooms)
    {
        List<HotelRooms> list = hotelRoomsService.selectHotelRoomsList(hotelRooms);
        ExcelUtil<HotelRooms> util = new ExcelUtil<HotelRooms>(HotelRooms.class);
        util.exportExcel(response, list, "酒店房间数据");
    }

    /**
     * 获取酒店房间详细信息
     */
//    @PreAuthorize("@ss.hasPermi('hotel:rooms:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hotelRoomsService.selectHotelRoomsById(id));
    }

    /**
     * 新增酒店房间
     */
    @PreAuthorize("@ss.hasPermi('hotel:rooms:add')")
    @Log(title = "酒店房间", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HotelRooms hotelRooms)
    {
        return toAjax(hotelRoomsService.insertHotelRooms(hotelRooms));
    }

    /**
     * 修改酒店房间
     */
    @PreAuthorize("@ss.hasPermi('hotel:rooms:edit')")
    @Log(title = "酒店房间", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HotelRooms hotelRooms)
    {
        return toAjax(hotelRoomsService.updateHotelRooms(hotelRooms));
    }

    /**
     * 删除酒店房间
     */
    @PreAuthorize("@ss.hasPermi('hotel:rooms:remove')")
    @Log(title = "酒店房间", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hotelRoomsService.deleteHotelRoomsByIds(ids));
    }
}
