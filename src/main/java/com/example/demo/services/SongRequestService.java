package com.example.demo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.sound.midi.Sequence;

import org.springframework.stereotype.Service;

import com.example.demo.models.SongRequest;
import com.example.demo.models.SongRequestDao;
import com.example.demo.models.User;
import com.example.demo.models.UserDao;
import com.example.demo.models.UserStatisticDto;

@Service
public class SongRequestService {
	
	private UserDao userDao;
	private SongRequestDao songRequestDao;
	
	public SongRequestService(UserDao userDao,
			SongRequestDao songRequestDao)
	{
		this.userDao = userDao;
		this.songRequestDao = songRequestDao;
	}
	
	public void QueueRequest(String requestId, String requestSequence, boolean completeRequest) 
	{	
		long id = Long.parseLong(requestId);
		SongRequest songRequestToQueue = songRequestDao.findOne(id);
		
		if (requestSequence.isEmpty())
		{
			SongRequest lastSongRequest = songRequestDao.findTop1ByOrderBySequenceDesc();
			songRequestToQueue.setSequence(lastSongRequest.getSequence() + 1);
		}
		else
		{
			int sequence = Integer.parseInt(requestSequence);
			Collection<SongRequest> songRequests = null;
			
			int upOrDown = 1; //up = -1, down = 1 
			
			if (songRequestToQueue.getSequence() < 0 || completeRequest)
			{
				upOrDown = completeRequest ? -1 : 1;
				songRequests = songRequestDao.findBySequenceGreaterThanEqual(completeRequest ? songRequestToQueue.getSequence() : sequence);			
			}
			else
			{
				int seq1 = songRequestToQueue.getSequence() < sequence ? songRequestToQueue.getSequence() : sequence;
				int seq2 = songRequestToQueue.getSequence() < sequence ? sequence : songRequestToQueue.getSequence();
				upOrDown = songRequestToQueue.getSequence() < sequence ? -1 : 1;		
				
				songRequests = songRequestDao.findBySequenceBetween(seq1, seq2);	
			}
					
			
			for(SongRequest sr : songRequests)
			{
				sr.setSequence(sr.getSequence() + (1 * upOrDown));
				songRequestDao.save(sr);
			}
			
			songRequestToQueue.setSequence(sequence);
			if(completeRequest)
			{
				songRequestToQueue.setComplete(true);
			}			
		}
		
		songRequestDao.save(songRequestToQueue);
		
	}
	
	public void ReSequenceRequests() {
		List<UserStatisticDto> userStatisticList = new ArrayList<UserStatisticDto>();
		Collection<User> users = userDao.findAll();
		Collection<SongRequest> songRequests = (Collection<SongRequest>) songRequestDao.findAll();
		long totalPendingRequests = songRequests.stream().filter(a -> !a.isComplete()).count();
		long totalCompletedRequests = songRequests.stream().filter(a -> a.isComplete()).count();
		List<SongRequest> requestsToUpdate = new ArrayList<SongRequest>();
		
		
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
					.filter(a -> !a.isComplete())
					.count());
			
			userStatisticDto.setSongsCompleted(
					userRequests
					.stream()
					.filter(a -> a.isComplete())
					.count());
			
			userStatisticDto.setPendingRelative(
					((float)userRequests
						.stream()
						.filter(a -> !a.isComplete())
						.count() / (totalPendingRequests == 0 ? 1 : totalPendingRequests)) * 100);
			
			userStatisticDto.setCompletedRelative(
					((float)userRequests
						.stream()
						.filter(a -> a.isComplete())
						.count() / (totalCompletedRequests == 0 ? 1 : totalCompletedRequests)) * 100);
			
			userStatisticList.add(userStatisticDto);
		}
		
		while (songRequests.size() != requestsToUpdate.size())
		{
			for (SongRequest songRequest : songRequests)
			{
				List<SongRequest> otherUserRequests = songRequests.stream().filter(a -> a.getUser() != songRequest.getUser()).collect(Collectors.toList());
				boolean otherUsersWaiting = false;
				for (SongRequest otherUserRequest : otherUserRequests)
				{
					if (songRequests
							.stream()
							.filter(a -> a.getUser().compareTo(otherUserRequest.getUser()) == 0)
							.count() > 0)
					{
						otherUsersWaiting = true;
						break;
					}				
				}
				
				
				if (requestsToUpdate
						.stream()
						.filter(a -> a.getUser().compareTo(songRequest.getUser()) == 0)
						.count() > 1 &&
					otherUsersWaiting)
				{
					//songRequests.remove(songRequest);
					//songRequests.add(songRequest);
				}
				else
				{
					requestsToUpdate.add(songRequest);
					//songRequests.remove(songRequest);
				}
			}
			
			int i = 0;
			for(SongRequest requestToUpdate : requestsToUpdate)
			{				
				requestToUpdate.setSequence(i);			
				songRequestDao.save(requestToUpdate);
				i++;			
			}
		}
		
		
	}
}
