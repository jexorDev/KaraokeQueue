package com.example.demo.models;

public class VoteResultDto {

	private User user;
	private long votes;
	
	public VoteResultDto(User user, long votes)
	{
		this.user = user;
		this.votes = votes;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public long getVotes() {
		return votes;
	}
	public void setVotes(long votes) {
		this.votes = votes;
	}
	
	
	
}
