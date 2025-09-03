package com.csetutorials.expensecircle.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version-number")
public class VersionNumberControler {

	@GetMapping
	public String getVersion() {
		return "2025-09-03 20.48.00";
	}

}
