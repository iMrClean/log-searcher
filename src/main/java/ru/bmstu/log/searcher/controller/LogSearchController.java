package ru.bmstu.log.searcher.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogSearchController {

    @GetMapping("/")
    public String main() {
	return "index";
    }

}
