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
import com.ruoyi.hotel.domain.HotelInvoices;
import com.ruoyi.hotel.service.IHotelInvoicesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 酒店账单Controller
 * 
 * @author lan
 * @date 2024-12-24
 */
@RestController
@RequestMapping("/hotel/invoices")
public class HotelInvoicesController extends BaseController
{
    @Autowired
    private IHotelInvoicesService hotelInvoicesService;

    /**
     * 查询酒店账单列表
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:list')")
    @GetMapping("/list")
    public TableDataInfo list(HotelInvoices hotelInvoices)
    {
        startPage();
        List<HotelInvoices> list = hotelInvoicesService.selectHotelInvoicesList(hotelInvoices);
        return getDataTable(list);
    }

    /**
     * 导出酒店账单列表
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:export')")
    @Log(title = "酒店账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HotelInvoices hotelInvoices)
    {
        List<HotelInvoices> list = hotelInvoicesService.selectHotelInvoicesList(hotelInvoices);
        ExcelUtil<HotelInvoices> util = new ExcelUtil<HotelInvoices>(HotelInvoices.class);
        util.exportExcel(response, list, "酒店账单数据");
    }

    /**
     * 获取酒店账单详细信息
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(hotelInvoicesService.selectHotelInvoicesById(id));
    }

    /**
     * 新增酒店账单
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:add')")
    @Log(title = "酒店账单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HotelInvoices hotelInvoices)
    {
        return toAjax(hotelInvoicesService.insertHotelInvoices(hotelInvoices));
    }

    /**
     * 修改酒店账单
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:edit')")
    @Log(title = "酒店账单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HotelInvoices hotelInvoices)
    {
        return toAjax(hotelInvoicesService.updateHotelInvoices(hotelInvoices));
    }

    /**
     * 删除酒店账单
     */
    @PreAuthorize("@ss.hasPermi('hotel:invoices:remove')")
    @Log(title = "酒店账单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hotelInvoicesService.deleteHotelInvoicesByIds(ids));
    }
}
