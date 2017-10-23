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

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView authenticate() {
		return new ModelAndView("home");		
	}
	
	@RequestMapping(value="/login/authenticate", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView authenticate(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("password") String password) {
		ModelAndView mv = new ModelAndView("redirect:/");
		User user = userDao.findByFirstNameAndLastNameAndPassword(firstName, lastName, password);
		
		if (user != null)
		{
			user.setSessionKey(httpSession.getId());
			httpSession.setAttribute("firstName", user.getFirstName());
			httpSession.setAttribute("lastName", user.getLastName());
			mv.addObject("user", user);
			
			user.setSessionKey(httpSession.getId());
			userDao.save(user);
		}
		else
		{
			mv.addObject("loginError", "Login Failed.  Verify your name and password are correct.");
		}
		
		return mv;		
	}
	
	@RequestMapping(value="/login/createUser", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView createUser(
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("password") String password) {
		ModelAndView mv = new ModelAndView("home");
		User user = userDao.findByFirstNameAndLastName(firstName, lastName);
		
		if (user == null)
		{
			user = new User();
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setSessionKey(httpSession.getId());
			user.setIsAdmin(false);
			user.setPassword(password);
			httpSession.setAttribute("firstName", user.getFirstName());
			httpSession.setAttribute("lastName", user.getLastName());
			mv.addObject("user", user);
			
			user.setSessionKey(httpSession.getId());
			userDao.save(user);
		}
		else
		{
			mv.addObject("loginError", "Account already exists.");
		}
		
		return mv;		
	}
}
