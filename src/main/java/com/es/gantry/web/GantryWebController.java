package com.es.gantry.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class GantryWebController {

    @RequestMapping("/")
    public String ui(Model model) {
        return "index";
    }
}