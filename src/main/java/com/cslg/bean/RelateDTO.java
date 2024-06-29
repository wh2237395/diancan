package com.cslg.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:关系数据类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelateDTO {
    //用户id
    private String userId;
    //业务id
    private Integer productId;
    //指数
    private Integer index;

}

