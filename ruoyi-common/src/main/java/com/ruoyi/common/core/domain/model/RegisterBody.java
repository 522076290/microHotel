package com.ruoyi.common.core.domain.model;

import lombok.Data;

/**
 * 用户注册对象
 * 
 * @author ruoyi
 */
@Data
public class RegisterBody extends LoginBody
{
    /**
     * 真实姓名
     */
    private String nickname;

    /**
     * 手机号码
     */
    private String phonenumber;

    /**
     * 邮箱
     */
    private String email;

}
