package com.example.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequestDao;
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
	
	@Autowired
	private SongRequestDao songRequestDao;
	
	@RequestMapping(value="/user/create", method=RequestMethod.GET)
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView("user/create");
		mv.addObject("user", new User());
		return mv;
	}
	
	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	public ModelAndView create(@Valid User user, BindingResult bindingResult)
	{		
		User existingUser = userDao.findByUsername(user.getUsername());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isAdmin = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_ADMIN") != null;
		
        if (bindingResult.hasErrors() ||
            existingUser != null) 
        {
        	ModelAndView mv = new ModelAndView("/user/create");
        	
        	if(existingUser != null)
        	{
        		mv.addObject("error", "Username already exists.");
        	}
        	else
        	{
        		mv.addObject("error", "All fields are required.");        		
        	}
        	
            return mv;
        }
        
		user.setEnabled(true);
		userDao.save(user);
		
		UserRole userRole = new UserRole();
		userRole.setUsername(user.getUsername());
		userRole.setRole("ROLE_USER");
		userRoleDao.save(userRole);
		return new ModelAndView(isAdmin ? "redirect:/admin" : "redirect:/home");
	}
	
	
}
