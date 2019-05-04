package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.example.demo.models.VoteResultDto;

@RestController
public class VotesController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private SongRequestDao songRequestDao;

	@RequestMapping(value="/vote", method=RequestMethod.GET)
	public ModelAndView index()
	{		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Boolean isKiosk = userRoleDao.findByUsernameAndRole(auth.getName(), "ROLE_KIOSK") != null;
		ModelAndView mv = new ModelAndView("votes/index");
		
		if (!isKiosk)
		{
			User votingUser = userDao.findByUsername(auth.getName());
			List<SongRequest> songsToVoteOn = songRequestDao.findAllByIsCompleteOrderBySequenceAscIdAsc(true)
					.stream()
					.filter(x -> x.getUser().getUsername() != votingUser.getUsername())
					.collect(Collectors.toList());
			Map<User, List<SongRequest>> userSongMap = songsToVoteOn
					.stream()
					.collect(Collectors.groupingBy(SongRequest::getUser, Collectors.toList()));
			
			mv.addObject("userSongMap", userSongMap);
			mv.addObject("votingUser", votingUser);	
		}
		
		mv.addObject("isKiosk", isKiosk);
		
		return mv;
	}
	
	@RequestMapping(value="/vote/{kiosk-user-id}", method=RequestMethod.GET)
	public ModelAndView index(@PathVariable(value="kiosk-user-id") String userIdFromKiosk)
	{			
		ModelAndView mv = new ModelAndView("fragments/voteList");
		
		User votingUser = userDao.findById(Long.parseLong(userIdFromKiosk));
		List<SongRequest> songsToVoteOn = songRequestDao.findAllByIsCompleteOrderBySequenceAscIdAsc(true)
				.stream()
				.filter(x -> x.getUser().getUsername() != votingUser.getUsername())
				.collect(Collectors.toList());
		Map<User, List<SongRequest>> userSongMap = songsToVoteOn
				.stream()
				.collect(Collectors.groupingBy(SongRequest::getUser, Collectors.toList()));
		
		mv.addObject("userSongMap", userSongMap);
		mv.addObject("votingUser", votingUser);	
		mv.addObject("isKiosk", true);
		
		return mv;
	}

	@RequestMapping(value = "/vote/{id}", method = RequestMethod.POST)
	public ModelAndView process(@PathVariable("id") String id, @RequestParam(value="voting-user-id", required=false) String votingUserId, RedirectAttributes redirectAttributes) {
		User votingUser = userDao.findById(Long.parseLong(votingUserId));
		User votedForUser = userDao.findById(Long.parseLong(id));

		votingUser.setVote(votedForUser);
		userDao.save(votingUser);
		
		redirectAttributes.addFlashAttribute("strongMessage", "Vote Casted");
		redirectAttributes.addFlashAttribute("mainMessage", "You can change your vote at any time by revisting the Vote page.");
		redirectAttributes.addFlashAttribute("alertClass", "alert-primary");
		return new ModelAndView("redirect:/home");
	}
	
	@RequestMapping(value="/vote/results", method=RequestMethod.GET)
	public ModelAndView results()
	{
		ModelAndView mv = new ModelAndView("votes/results");
		List<User> users = userDao.findAll();
		List<User> usersWhoVoted = userDao.findAllByVoteNotNull();
		
		List<VoteResultDto> results = new ArrayList<>();

		for (User user : users)
		{
			long votes = users
					.stream()
					.filter(x -> x.getVote() != null && x.getUsername().equals(user.getUsername()))					
					.count();
			
			results.add(new VoteResultDto(user, votes));
		}
		
		results.sort(Comparator.comparing(VoteResultDto::getVotes).reversed());
		
		mv.addObject("results", results);
		mv.addObject("usersWhoVoted", usersWhoVoted);
		
		return mv;
	}
}
