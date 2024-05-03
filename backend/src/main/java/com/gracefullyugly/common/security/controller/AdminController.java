package com.gracefullyugly.common.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdminController {

    @GetMapping("/admin")
    public String adminP() {

        return "접근권한 Admin 허용 TEST 통과";
    }

    @GetMapping("/test")
    public String testP() {

        return "접근권한 모두 허용 TEST 통과";
    }
}