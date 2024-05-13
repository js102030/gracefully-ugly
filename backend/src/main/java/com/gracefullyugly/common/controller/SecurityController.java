package com.gracefullyugly.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SecurityController {

    @PostMapping("/login")
    public String login() {
        return "main";
    }
}
