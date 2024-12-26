package com.ruoyi.hotel.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 酒店账单对象 hotel_invoices
 * 
 * @author lan
 * @date 2024-12-24
 */
public class HotelInvoices extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 账单唯一标识ID */
    private Long id;

    /** 订单ID，关联订单信息表 */
    @Excel(name = "订单ID，关联订单信息表")
    private Long orderId;

    /** 客户ID，关联客户信息表 */
    @Excel(name = "客户ID，关联客户信息表")
    private Long customerId;

    /** 房间总费用 */
    @Excel(name = "房间总费用")
    private BigDecimal totalRoomCharge;

    /** 额外服务费用 */
    @Excel(name = "额外服务费用")
    private BigDecimal additionalCharges;

    /** 折扣金额 */
    @Excel(name = "折扣金额")
    private BigDecimal discount;

    /** 税费 */
    @Excel(name = "税费")
    private BigDecimal tax;

    /** 应付总额 */
    @Excel(name = "应付总额")
    private BigDecimal totalDue;

    /** 支付状态的整型表示，对应字典表中的状态 */
    @Excel(name = "支付状态的整型表示，对应字典表中的状态")
    private Long paymentStatus;

    /** 支付日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentDate;

    /** 账单开具日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "账单开具日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date issueDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }
    public void setTotalRoomCharge(BigDecimal totalRoomCharge) 
    {
        this.totalRoomCharge = totalRoomCharge;
    }

    public BigDecimal getTotalRoomCharge() 
    {
        return totalRoomCharge;
    }
    public void setAdditionalCharges(BigDecimal additionalCharges) 
    {
        this.additionalCharges = additionalCharges;
    }

    public BigDecimal getAdditionalCharges() 
    {
        return additionalCharges;
    }
    public void setDiscount(BigDecimal discount) 
    {
        this.discount = discount;
    }

    public BigDecimal getDiscount() 
    {
        return discount;
    }
    public void setTax(BigDecimal tax) 
    {
        this.tax = tax;
    }

    public BigDecimal getTax() 
    {
        return tax;
    }
    public void setTotalDue(BigDecimal totalDue) 
    {
        this.totalDue = totalDue;
    }

    public BigDecimal getTotalDue() 
    {
        return totalDue;
    }
    public void setPaymentStatus(Long paymentStatus) 
    {
        this.paymentStatus = paymentStatus;
    }

    public Long getPaymentStatus() 
    {
        return paymentStatus;
    }
    public void setPaymentDate(Date paymentDate) 
    {
        this.paymentDate = paymentDate;
    }

    public Date getPaymentDate() 
    {
        return paymentDate;
    }
    public void setIssueDate(Date issueDate) 
    {
        this.issueDate = issueDate;
    }

    public Date getIssueDate() 
    {
        return issueDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("customerId", getCustomerId())
            .append("totalRoomCharge", getTotalRoomCharge())
            .append("additionalCharges", getAdditionalCharges())
            .append("discount", getDiscount())
            .append("tax", getTax())
            .append("totalDue", getTotalDue())
            .append("paymentStatus", getPaymentStatus())
            .append("paymentDate", getPaymentDate())
            .append("issueDate", getIssueDate())
            .toString();
    }
}
