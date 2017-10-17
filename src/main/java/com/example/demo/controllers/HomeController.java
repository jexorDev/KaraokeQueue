package com.example.demo.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	@RequestMapping("/home")
	public String home() {
		//model.addAttribute("message", "adios");
		//return model;
		return "here";
	}
}
