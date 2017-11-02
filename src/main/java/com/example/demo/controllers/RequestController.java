package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;

@RestController
public class RequestController {
	
	@Autowired
	private SongRequestDao songRequestDao;
	
	@Autowired
	private UserDao userDao;
		
	@RequestMapping(value="/request/create", method=RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("request/create");
		
		List<User> users = userDao.findAll();
		
		mv.addObject("songRequest", new SongRequest());
		mv.addObject("users", users);
		return mv;		
	}
	
	@RequestMapping(value="/request/create", method=RequestMethod.POST)
	public ModelAndView create(@ModelAttribute SongRequest songRequest) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth.getAuthorities().stream().filter(a -> a.getAuthority() == "ROLE_ADMIN").count() > 0;
		
		//admin have ability to choose any user
		if (songRequest.getUser() == null)
		{
			User user = userDao.findByUsername(auth.getName());			
			songRequest.setUser(user);	
		}
		
		songRequest.setSequence(-1);
		
		songRequestDao.save(songRequest);
		
		ModelAndView mv = new ModelAndView("redirect:/home");				
		return mv;		
	}
	
	@RequestMapping(value="/request/delete/{id}", method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable("id") String id)
	{		
		SongRequest songRequest = songRequestDao.findOne(Long.parseLong(id));
		
		songRequestDao.delete(songRequest);
		
		return new ModelAndView("redirect:/home");
	}
}
