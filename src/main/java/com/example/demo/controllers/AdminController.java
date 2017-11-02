package com.example.demo.controllers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.mysql.fabric.xmlrpc.base.Array;

@RestController
public class AdminController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SongRequestDao songRequestDao;
	

	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public ModelAndView index()
	{
		UserService userService = new UserService(userDao, songRequestDao);
		
		ModelAndView mv = new ModelAndView("admin/index");
		
		List<SongRequest> allRequests = songRequestDao.findAllByOrderBySequence();				
		List<SongRequest> pendingRequests = songRequestDao.findAllByIsCompleteOrderBySequence(false);
				
		float pendingPercent = 100 * ((float)pendingRequests.spliterator().getExactSizeIfKnown() / allRequests.spliterator().getExactSizeIfKnown());
		float completePercent = 100 - pendingPercent;
		
		mv.addObject("users", userDao.findAll());
		mv.addObject("currentRequests", pendingRequests);
		mv.addObject("pendingPercent", pendingPercent);
		mv.addObject("completePercent", completePercent);
		mv.addObject("userStatistics", userService.GetUserStatistics());
		return mv;
	}
	
	@RequestMapping(value="/admin/complete/{id}", method=RequestMethod.POST)
	public ModelAndView complete(@PathVariable("id") String id)
	{
		SongRequestService songRequestService = new SongRequestService(userDao, songRequestDao);		
				
		songRequestService.QueueRequest(id, "-1", true);
		
		ModelAndView mv = new ModelAndView("redirect:/admin");		
		return mv;
	}
	
	@RequestMapping(value="/admin/queue/{id}", method=RequestMethod.POST)
	public ModelAndView complete(@PathVariable("id") String id, @RequestParam(value="sequence", required=false) String sequence)
	{
		SongRequestService songRequestService = new SongRequestService(userDao, songRequestDao);				
				
		songRequestService.QueueRequest(id, sequence, false);
		
		
		ModelAndView mv = new ModelAndView("redirect:/admin");		
		return mv;
	}
	
	@RequestMapping(value="/admin/resequence", method=RequestMethod.GET)
	public ModelAndView resequence()
	{
		SongRequestService songRequestService = new SongRequestService(userDao, songRequestDao);
		songRequestService.ReSequenceRequests();
		
		ModelAndView mv = new ModelAndView("redirect:/admin");		
		return mv;
	}
	
}
