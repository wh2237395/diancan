package com.cslg.request;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

/**
 * 编程小石头：2501902696（微信）
 */
@Data
public class OrderReq {
    @NotEmpty(message = "姓名必填")
    private String name;//买家姓名
    @NotEmpty(message = "手机号必填")
    private String phone;//买家手机号
    @NotEmpty(message = "桌号必填")
    private String address;//买家桌号
    @NotEmpty(message = "openid必填")
    private String openid;//买家微信openid
    @NotEmpty(message = "购物车不能为空")
    private String items;//购物车
}
