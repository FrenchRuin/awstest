package com.example.awstest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String test() {
        return "test";
    }

    @GetMapping("/test")
    public String home() {
        return "도커 ddd";
    }
}
