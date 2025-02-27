package com.cslg.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 作者：编程小石头
 * 添加类目表单所需
 */
@Data
public class LeimuReq {
    private Integer leimuId;
    @NotEmpty(message = "类目名必填")
    private String leimuName;//类目名字
    @NotNull(message = "类目type必填")
    private Integer leimuType;//类目编号
}
