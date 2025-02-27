package com.cslg.utils;

import com.cslg.enumeration.CodeNumEnum;

/**
 * 编程小石头：2501902696（微信）
 */
public class EnumUtil {

    /*
    * 获取枚举里的code值
    * */
    public static <T extends CodeNumEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }

}
