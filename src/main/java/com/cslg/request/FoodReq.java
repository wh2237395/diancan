package com.cslg.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 编程小石头：2501902696（微信）
 */
@Data
public class FoodReq {
    private Integer foodId;
    @NotEmpty(message = "菜品名必填")
    private String foodName;
    @NotNull(message = "菜品价格必填")
    private BigDecimal foodPrice;
    private Integer foodStock;
    private String foodDesc;
    @NotEmpty(message = "菜品图必填")
    private String foodIcon;

    private Integer leimuType;
}
