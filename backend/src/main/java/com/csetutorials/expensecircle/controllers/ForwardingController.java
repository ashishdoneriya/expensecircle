package com.csetutorials.expensecircle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class ForwardingController {

	@GetMapping // Match all paths without a dot (e.g., .css, .js, etc.)
	public String forwardToIndex() {
		return "forward:/index.html";
	}
}