package com.ruoyi.hotel.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 酒店订单对象 hotel_orders
 * 
 * @author Lan
 * @date 2024-12-24
 */
public class HotelOrders extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单唯一标识ID */
    private Long id;

    /** 订单号码，用于客户查询和酒店管理 */
    @Excel(name = "订单号码，用于客户查询和酒店管理")
    private String orderNumber;

    /** 客户ID，关联客户信息表 */
    @Excel(name = "客户ID，关联客户信息表")
    private Long customerId;

    /** 房间ID，关联房间信息表 */
    @Excel(name = "房间ID，关联房间信息表")
    private Long roomId;

    /** 入住日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 离店日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离店日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 预订的房间数量 */
    @Excel(name = "预订的房间数量")
    private Long numOfRooms;

    /** 订单总价 */
    @Excel(name = "订单总价")
    private BigDecimal totalPrice;

    /** 预订日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预订日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;

    /** 订单状态的整型表示，对应字典表中的状态 */
    @Excel(name = "订单状态的整型表示，对应字典表中的状态")
    private Long status;

    /** 商家接单日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "商家接单日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date merchantAcceptDate;

    /** 商家接单状态的整型表示，对应字典表中的状态 */
    @Excel(name = "商家接单状态的整型表示，对应字典表中的状态")
    private Long merchantAcceptStatus;

    /** 客户评分，1到5星 */
    @Excel(name = "客户评分，1到5星")
    private Long customerRating;

    /** 客户评价内容 */
    @Excel(name = "客户评价内容")
    private String customerReview;

    /** 评价日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评价日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reviewDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderNumber(String orderNumber) 
    {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() 
    {
        return orderNumber;
    }
    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }
    public void setRoomId(Long roomId) 
    {
        this.roomId = roomId;
    }

    public Long getRoomId() 
    {
        return roomId;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }
    public void setNumOfRooms(Long numOfRooms) 
    {
        this.numOfRooms = numOfRooms;
    }

    public Long getNumOfRooms() 
    {
        return numOfRooms;
    }
    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }
    public void setOrderDate(Date orderDate) 
    {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() 
    {
        return orderDate;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setMerchantAcceptDate(Date merchantAcceptDate) 
    {
        this.merchantAcceptDate = merchantAcceptDate;
    }

    public Date getMerchantAcceptDate() 
    {
        return merchantAcceptDate;
    }
    public void setMerchantAcceptStatus(Long merchantAcceptStatus) 
    {
        this.merchantAcceptStatus = merchantAcceptStatus;
    }

    public Long getMerchantAcceptStatus() 
    {
        return merchantAcceptStatus;
    }
    public void setCustomerRating(Long customerRating) 
    {
        this.customerRating = customerRating;
    }

    public Long getCustomerRating() 
    {
        return customerRating;
    }
    public void setCustomerReview(String customerReview) 
    {
        this.customerReview = customerReview;
    }

    public String getCustomerReview() 
    {
        return customerReview;
    }
    public void setReviewDate(Date reviewDate) 
    {
        this.reviewDate = reviewDate;
    }

    public Date getReviewDate() 
    {
        return reviewDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNumber", getOrderNumber())
            .append("customerId", getCustomerId())
            .append("roomId", getRoomId())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("numOfRooms", getNumOfRooms())
            .append("totalPrice", getTotalPrice())
            .append("orderDate", getOrderDate())
            .append("status", getStatus())
            .append("merchantAcceptDate", getMerchantAcceptDate())
            .append("merchantAcceptStatus", getMerchantAcceptStatus())
            .append("customerRating", getCustomerRating())
            .append("customerReview", getCustomerReview())
            .append("reviewDate", getReviewDate())
            .toString();
    }
}
