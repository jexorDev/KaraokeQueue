package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping(value= {"/"})
	public String welcome()
	{
		return "welcome";
	}
	

	@RequestMapping(value= {"/home"})
	public String home()
	{
		return "home";
	}
	
	@RequestMapping(value= {"/admin"})
	public String admin()
	{
		return "admin/index";
	}

	@RequestMapping(value= {"/request/create"})
	public String index()
	{
		return "request/create";
	}
	
}
