package com.cslg.push;

import lombok.Data;

/*
 * 编程小石头
 * 用来封装订阅消息内容
 * */
@Data
public class TemplateData {
    private String value;//

    public TemplateData(String value) {
        this.value = value;
    }

}