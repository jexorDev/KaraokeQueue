package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;
import com.example.demo.models.UserStatisticDto;

@Service
public class UserService {
	
	private UserDao userDao;
	private SongRequestDao songRequestDao;
	
	public UserService(UserDao userDao,
			SongRequestDao songRequestDao)
	{
		this.userDao = userDao;
		this.songRequestDao = songRequestDao;
	}
	
	public List<UserStatisticDto> GetUserStatistics() {
		List<UserStatisticDto> userStatisticList = new ArrayList<UserStatisticDto>();
		Collection<User> users = userDao.findAll();
		Collection<SongRequest> songRequests = (Collection<SongRequest>) songRequestDao.findAll();
		long totalPendingRequests = songRequests.stream().filter(a -> !a.isComplete() && a.getSequence() < 0).count();
		long totalCompletedRequests = songRequests.stream().filter(a -> a.isComplete()).count();
		long totalQueuedRequests = songRequests.stream().filter(a -> a.getSequence() > -1).count();
		
		for (User user : users) {
			UserStatisticDto userStatisticDto = new UserStatisticDto();
			userStatisticDto.setUser(user);
			Collection<SongRequest> userRequests = songRequests
					.stream()
					.filter(a -> a.getUser().getId() == user.getId())
					.collect(Collectors.toList());
			
			userStatisticDto.setSongsPending(
					userRequests
					.stream()
					.filter(a -> !a.isComplete() && a.getSequence() < 0)
					.count());
			
			userStatisticDto.setSongsCompleted(
					userRequests
					.stream()
					.filter(a -> a.isComplete())
					.count());
			
			userStatisticDto.setSongsQueued(
					userRequests
					.stream()
					.filter(a -> a.getSequence() > -1)
					.count());
			
			userStatisticDto.setPendingRelative(
					((float)userRequests
						.stream()
						.filter(a -> !a.isComplete()  && a.getSequence() < 0)
						.count() / (totalPendingRequests == 0 ? 1 : totalPendingRequests)) * 100);
			
			userStatisticDto.setCompletedRelative(
					((float)userRequests
						.stream()
						.filter(a -> a.isComplete())
						.count() / (totalCompletedRequests == 0 ? 1 : totalCompletedRequests)) * 100);
			
			userStatisticDto.setQueuedRelative(
					((float)userRequests
						.stream()
						.filter(a -> a.getSequence() > -1)
						.count() / (totalQueuedRequests == 0 ? 1 : totalQueuedRequests)) * 100);
		
			
			userStatisticList.add(userStatisticDto);
		}
		
		return userStatisticList;
	}
}
