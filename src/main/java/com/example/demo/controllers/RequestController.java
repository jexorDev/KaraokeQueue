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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;
import com.example.demo.models.UserRole;
import com.example.demo.models.UserRoleDao;

@RestController
public class RequestController {
	
	@Autowired
	private SongRequestDao songRequestDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@RequestMapping(value="/request/view/{userId}", method=RequestMethod.GET)
	public ModelAndView view(@PathVariable(value="userId") String userId) {
		ModelAndView mv = new ModelAndView("fragments/requestsList");
		List<SongRequest> songRequests = songRequestDao.findByUserIdAndIsCompleteOrderById(Long.parseLong(userId),  false);				
		mv.addObject("songRequests", songRequests);
		
		return mv;		
	}
		
	@RequestMapping(value="/request/create", method=RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("request/create");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isKiosk = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_KIOSK") != null;
		
		List<User> users = userDao.findAllByOrderByFirstName();
		
		mv.addObject("songRequest", new SongRequest());
		mv.addObject("users", users);
		mv.addObject("isKiosk", isKiosk);
		return mv;		
	}
	
	@RequestMapping(value="/request/create", method=RequestMethod.POST)
	public ModelAndView create(@ModelAttribute SongRequest songRequest, @RequestParam(value="kiosk-user-id", required=false) String userIdFromKiosk, RedirectAttributes redirectAttributes) 
	{		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isAdmin = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_ADMIN") != null;
		Boolean isKiosk = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_KIOSK") != null;
		
		if (isKiosk)
		{
			User user = userDao.findById(Long.parseLong(userIdFromKiosk));			
			songRequest.setUser(user);	
		}
		else if (!isAdmin)
		{
			//admin will have already chosen user
			//in the case of regular user, need to set user on song request with logged in user
			User user = userDao.findByUsername(auth.getName());			
			songRequest.setUser(user);	
		}
		
		songRequest.setSequence(-1);
		
		songRequestDao.save(songRequest);
		
				
		ModelAndView mv;
		
		if (isAdmin)
		{
			mv = new ModelAndView("redirect:/admin");	
		}
		else if(isKiosk)
		{
			redirectAttributes.addFlashAttribute("strongMessage", "Request submitted");
			redirectAttributes.addFlashAttribute("mainMessage", "You can monitor the status of your request by doing an inquiry at the kiosk or logging into your account from your mobile device.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-primary");
			mv = new ModelAndView("redirect:/kiosk");			
		}
		else
		{
			redirectAttributes.addFlashAttribute("strongMessage", "Request submitted");
			redirectAttributes.addFlashAttribute("mainMessage", "You can monitor the status by refreshing this page.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-primary");
			
			mv = new ModelAndView("redirect:/home");
		}
						
		return mv;		
	}
	
	@RequestMapping(value="/request/delete/{id}", method=RequestMethod.POST)
	public ModelAndView delete(@PathVariable("id") String id)
	{		
		SongRequest songRequest = songRequestDao.findOne(Long.parseLong(id));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isAdmin = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_ADMIN") != null;
		
		songRequestDao.delete(songRequest);
		
		return new ModelAndView(isAdmin ? "redirect:/admin" : "redirect:/home");
	}
}
