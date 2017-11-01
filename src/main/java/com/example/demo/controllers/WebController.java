package com.example.demo.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;

@Controller
public class WebController {
	
	@Autowired
	private SongRequestDao songRequestDao;
	
	@Autowired
	private UserDao userDao;

	@RequestMapping(value= {"/"})
	public String index()
	{
		return "redirect:/home";
	}	

	@RequestMapping(value= {"/home"})
	public ModelAndView home()
	{		
		ModelAndView mv = new ModelAndView("home");
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		User user = userDao.findByUsername(auth.getName());
		
		List<SongRequest> songRequests = songRequestDao.findByUserId(user.getId());		
		List<SongRequest> nextRequests = songRequestDao.findTop3BySequenceGreaterThanEqualOrderBySequence(0);
		Collections.sort(nextRequests, (a,b) -> a.getSequence() > b.getSequence() ? 0 : 1);
		
		mv.addObject("songRequests", songRequests);
		mv.addObject("nextRequests", nextRequests);
		
		return mv;
	}	
	
}
