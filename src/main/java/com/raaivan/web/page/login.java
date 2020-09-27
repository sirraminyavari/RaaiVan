package com.raaivan.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class login {
    @GetMapping("/")
    public String getIndexPage(){
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }
}
