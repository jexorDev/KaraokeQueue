package com.example.demo.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.UserDao;
import com.example.demo.services.SongRequestService;
import com.example.demo.services.UserService;

@RestController
public class KioskController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SongRequestDao songRequestDao;
	

	
	@RequestMapping(value="/kiosk", method=RequestMethod.GET)
	public ModelAndView index()
	{			
		ModelAndView mv = new ModelAndView("kiosk/index");
		
		return mv;
	}
	
}
