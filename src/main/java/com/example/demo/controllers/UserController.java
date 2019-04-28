package com.example.demo.controllers;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Credentials;
import com.example.demo.models.CredentialsAjaxResponseBody;
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
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isKiosk = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_KIOSK") != null;
		
		ModelAndView mv = new ModelAndView("user/create");
		mv.addObject("user", new User());
		mv.addObject("isKiosk", isKiosk);
		return mv;
	}
	
    @PostMapping("/user/get/id")
    public ResponseEntity<?> getSearchResultViaAjax(
            @Valid @RequestBody Credentials credentials, Errors errors) {

        CredentialsAjaxResponseBody result = new CredentialsAjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                        .stream().map(x -> x.getDefaultMessage())
                        .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        
        User user = userDao.findByUsername(credentials.getUsername());
		
		if (user != null && user.getPassword().equals(credentials.getPassword())) {
			result.setMsg("success");
			result.setResult(user.getId());
		}
		else
		{
			result.setMsg("no match");
			result.setResult(-1);
		}
     

        return ResponseEntity.ok(result);

    }
    
	
	@RequestMapping(value="/user/create", method=RequestMethod.POST)
	public ModelAndView create(@Valid User user, @RequestParam(value = "isKiosk", required = false) String isKioskParm, BindingResult bindingResult, RedirectAttributes redirectAttributes)
	{		
		User existingUser = userDao.findByUsername(user.getUsername());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isAdmin = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_ADMIN") != null;
		Boolean isKiosk = Boolean.parseBoolean(isKioskParm);
		
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
		
		if (isAdmin)
		{
			return new ModelAndView("redirect:/admin");
		}
		if(isKiosk)
		{
			redirectAttributes.addFlashAttribute("strongMessage", "Account created");
			redirectAttributes.addFlashAttribute("mainMessage", "You can begin submitting song requests by using the kiosk or logging into your account from your mobile device.");
			redirectAttributes.addFlashAttribute("alertClass", "alert-primary");
			
			return new ModelAndView("redirect:/kiosk");
		}
		else
		{
			return new ModelAndView("redirect:/home");
		}		
			
	}
	
	
}
