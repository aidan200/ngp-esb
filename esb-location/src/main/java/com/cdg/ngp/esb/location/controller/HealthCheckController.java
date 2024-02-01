package com.cdg.ngp.esb.location.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName HealthCheckController
 * @Description TODO
 * @Author siy
 * @Date 2024/2/1 15:21
 * @Version 1.0
 */

@RestController
public class HealthCheckController {

    @GetMapping("/healthCheck")
    public String healthCheck(){
        return "success";
    }
}
