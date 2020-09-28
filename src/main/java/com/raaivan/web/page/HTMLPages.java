package com.raaivan.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class HTMLPages {
    @GetMapping("/")
    public String getIndexPage(Model model){
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login.html";
    }
}
