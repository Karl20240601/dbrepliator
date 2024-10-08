package com.crazymaker.cloud.nacos.demo.config.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simple")
@Api(tags = "echo 演示")
public class PingController {

    @GetMapping("/ping")
    public String ping() throws InterruptedException {
//        Thread.sleep(800);
        return "pong";
    }


}
