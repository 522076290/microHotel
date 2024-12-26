package com.ruoyi.hotel.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 酒店房间对象 hotel_rooms
 * 
 * @author Lan
 * @date 2024-12-25
 */
public class HotelRooms extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间唯一标识ID */
    private Long id;

    /** 房间号码 */
    @Excel(name = "房间号码")
    private String roomNumber;

    /** 房间标题 */
    @Excel(name = "房间标题")
    private String title;

    /** 房间类型，如单人间、双人间、套房等 */
    @Excel(name = "房间类型，如单人间、双人间、套房等")
    private String type;

    /** 房间价格 */
    @Excel(name = "房间价格")
    private BigDecimal price;

    /** 房间状态的整型表示，对应字典表中的状态 */
    @Excel(name = "房间状态的整型表示，对应字典表中的状态")
    private Long status;

    /** 房间详细描述 */
    @Excel(name = "房间详细描述")
    private String description;

    /** 房间图片的存储路径或URL */
    @Excel(name = "房间图片的存储路径或URL")
    private String image;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRoomNumber(String roomNumber) 
    {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() 
    {
        return roomNumber;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roomNumber", getRoomNumber())
            .append("title", getTitle())
            .append("type", getType())
            .append("price", getPrice())
            .append("status", getStatus())
            .append("description", getDescription())
            .append("image", getImage())
            .toString();
    }
}
