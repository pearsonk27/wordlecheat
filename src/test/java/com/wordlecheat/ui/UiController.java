package com.wordlecheat.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UiController {

    @RequestMapping(value = "/")
	public String index() {
		return "index";
	}
}
