package com.cslg.enumeration;

import lombok.Getter;

/**
 * 管理员身份
 * 编程小石头：2501902696（微信）
 */
@Getter
public enum AdminStatusEnum implements CodeNumEnum {
    //2：管理员可以管理所有，1：员工只可以管理菜品和订单
    SUPER_ADMIN(2, "管理员"),
    YUANGONG(1, "员工");

    private Integer code;
    private String message;

    AdminStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
