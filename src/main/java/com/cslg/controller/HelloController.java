package com.cslg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "项目可以正常的跑起来了";
    }
}
