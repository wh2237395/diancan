package com.cslg.bean;

import com.cslg.enumeration.OrderStatusEnum;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 用户订单总单

 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class WxOrderRoot {

    @Id
    @GeneratedValue
    private Integer orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;//桌号
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();//订单状态, 默认为0新下单.
    private Integer payStatus = 0;//支付状态, 默认为0未支付
    private Integer cuidan = 0;//被催单次数
    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;

}
