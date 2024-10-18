package com.example.myproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String indexroot() {
        return "index";
    }

    @GetMapping("index.html")
    public String index() {
        return "index";
    }

    @GetMapping("product.html")
    public String product() {
        return "product";
    }

    @GetMapping("thankyou.html")
    public String thankyou() {
        return "thankyou";
    }

    @GetMapping("profile.html")
    public String profile() {
        return "profile";
    }
}
