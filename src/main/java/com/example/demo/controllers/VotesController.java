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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
	private SongRequestDao songRequestDao;

	@RequestMapping(value="/vote", method=RequestMethod.GET)
	public ModelAndView index()
	{
		ModelAndView mv = new ModelAndView("votes/index");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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
		
		return mv;
	}

	@RequestMapping(value = "/vote/{id}", method = RequestMethod.POST)
	public ModelAndView process(@PathVariable("id") String id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User votingUser = userDao.findByUsername(auth.getName());
		User votedForUser = userDao.findById(Long.parseLong(id));

		votingUser.setVote(votedForUser);
		userDao.save(votingUser);

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
