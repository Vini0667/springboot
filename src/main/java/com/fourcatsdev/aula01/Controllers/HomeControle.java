package com.fourcatsdev.aula01.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeControle {

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("msBemVindo", "Bem vindo a biblioteca");
        return "publica-index";
    }
}
