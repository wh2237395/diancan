package com.cslg.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品(包含类目)
 */
@Data
public class LeimuVO {

    @JsonProperty("name")
    private String leimuName;

    @JsonProperty("type")
    private Integer leimuType;

    @JsonProperty("foods")
    private List<FoodRes> foodResList;
}
