package com.example.demo.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.models.User;
import com.example.demo.models.UserDao;

@RestController
public class LoginController {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("login");		
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView login() {
		return new ModelAndView("home");		
	}
	

	
	
}
