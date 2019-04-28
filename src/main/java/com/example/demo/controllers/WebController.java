package com.example.demo.controllers;

import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;
import com.example.demo.models.UserRoleDao;

@Controller
public class WebController {
	
	@Autowired
	private SongRequestDao songRequestDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;

	@RequestMapping(value= {"/"})
	public String index()
	{
		return "redirect:/home";
	}	
	
	@GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

	@RequestMapping(value= {"/home"})
	public ModelAndView home()
	{					
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userDao.findByUsername(auth.getName());				
		boolean isAdmin = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_ADMIN") != null;
		boolean isKiosk = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_KIOSK") != null;    
	  		
		List<SongRequest> songRequests = songRequestDao.findByUserIdAndIsCompleteOrderById(user.getId(), false);		
		List<SongRequest> nextRequests = songRequestDao.findTop3BySequenceGreaterThanEqualOrderBySequence(0);
		Collections.sort(nextRequests, (a,b) -> a.getSequence() > b.getSequence() ? 0 : 1);
		
		ModelAndView mv;
		
		if (!isKiosk)
		{
			mv = new ModelAndView("home");
			mv.addObject("songRequests", songRequests);
			mv.addObject("nextRequests", nextRequests);
			mv.addObject("isAdmin", isAdmin);			
		}
		else
		{
			mv = new ModelAndView("kiosk/index");
		}		
		
		return mv;
	}	
	
}
