package com.ruoyi.hotel.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.hotel.domain.resp.HotelOrdersResp;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.hotel.domain.HotelOrders;
import com.ruoyi.hotel.service.IHotelOrdersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店订单Controller
 * 
 * @author Lan
 * @date 2024-12-24
 */
@RestController
@RequestMapping("/hotel/orders")
public class HotelOrdersController extends BaseController
{
    @Autowired
    private IHotelOrdersService hotelOrdersService;

    /**
     * 查询酒店订单列表
     */
    @PreAuthorize("@ss.hasPermi('hotel:orders:list')")
    @GetMapping("/list")
    public TableDataInfo list(HotelOrders hotelOrders)
    {
        startPage();
        List<HotelOrdersResp> list = hotelOrdersService.selectHotelOrdersList(hotelOrders);
        return getDataTable(list);
    }

    /**
     * 导出酒店订单列表
     */
//    @PreAuthorize("@ss.hasPermi('hotel:orders:export')")
//    @Log(title = "酒店订单", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, HotelOrders hotelOrders)
//    {
//        List<HotelOrders> list = hotelOrdersService.selectHotelOrdersList(hotelOrders);
//        ExcelUtil<HotelOrders> util = new ExcelUtil<HotelOrders>(HotelOrders.class);
//        util.exportExcel(response, list, "酒店订单数据");
//    }

    /**
     * 获取酒店订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('hotel:orders:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hotelOrdersService.selectHotelOrdersById(id));
    }

    /**
     * 新增酒店订单
     */
    @PreAuthorize("@ss.hasPermi('hotel:orders:add')")
    @Log(title = "酒店订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HotelOrders hotelOrders)
    {
        return success(hotelOrdersService.insertHotelOrders(hotelOrders));
    }

    /**
     * 修改酒店订单
     */
    @PreAuthorize("@ss.hasPermi('hotel:orders:edit')")
    @Log(title = "酒店订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HotelOrders hotelOrders)
    {
        return toAjax(hotelOrdersService.updateHotelOrders(hotelOrders));
    }

    /**
     * 删除酒店订单
     */
    @PreAuthorize("@ss.hasPermi('hotel:orders:remove')")
    @Log(title = "酒店订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hotelOrdersService.deleteHotelOrdersByIds(ids));
    }

    /**
     * 获取一周的酒店订单列表
     */
    @GetMapping("/week")
    @PreAuthorize("@ss.hasPermi('hotel:orders:list')")
    @Log(title = "酒店订单", businessType = BusinessType.OTHER)
    public AjaxResult getWeekOrders(@RequestParam(name = "weekOffset", defaultValue = "0") int weekOffset,
                                    HotelOrders hotelOrders) {
        List<HotelOrders> list = hotelOrdersService.selecWeekHotelOrdersList(weekOffset,hotelOrders);
        return success(list);
    }
}
