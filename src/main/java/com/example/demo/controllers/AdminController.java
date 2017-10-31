package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.UserDao;
import com.example.demo.services.SongRequestService;
import com.example.demo.services.UserService;

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
		
		Iterable<SongRequest> allRequests = songRequestDao.findAll();
		Iterable<SongRequest> pendingRequests = songRequestDao.findAllByIsComplete(false);
		
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
		SongRequest songRequest = songRequestDao.findOne(Long.parseLong(id));
		songRequest.setComplete(true);
		songRequest.setSequence(-1);
		songRequestDao.save(songRequest);
		
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
