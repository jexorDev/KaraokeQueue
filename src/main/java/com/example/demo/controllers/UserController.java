package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.User;
import com.example.demo.models.UserDao;
import com.example.demo.models.UserRole;
import com.example.demo.models.UserRoleDao;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@RequestMapping(value="/user/create", method=RequestMethod.GET)
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView("user/create");
		mv.addObject("user", new User());
		return mv;
	}
	
	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	public ModelAndView create(@ModelAttribute User user)
	{
		user.setEnabled(true);
		userDao.save(user);
		
		UserRole userRole = new UserRole();
		userRole.setUsername(user.getUsername());
		userRole.setRole("ROLE_USER");
		userRoleDao.save(userRole);
		return new ModelAndView("redirect:/home");
	}
}
