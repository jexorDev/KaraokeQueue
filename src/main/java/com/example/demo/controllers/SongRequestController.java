package com.example.demo.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;

@RestController
public class SongRequestController {
	
	@Autowired
	private HttpSession httpSession;
	
	@RequestMapping(value="/songRequest/create", method=RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("songRequestCreate");
		mv.addObject("songRequest", new SongRequest());		
		return mv;		
	}
	
	@RequestMapping(value="/songRequest/create", method=RequestMethod.POST)
	public ModelAndView create(@ModelAttribute SongRequest songRequest) {
		ModelAndView mv = new ModelAndView("redirect:/");
		mv.addObject("songRequest", new SongRequest());		
		return mv;		
	}
}
