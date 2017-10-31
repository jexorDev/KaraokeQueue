package com.example.demo.models;

public class UserStatisticDto {

	private long songsPending;
	private long songsCompleted;
	private float pendingRelative;
	private float completedRelative;
	private User user;
	private String rowColor;
	
	
	public String getRowColor() {
		if (pendingRelative > 50 || completedRelative > 50)
			return "danger";
		if (pendingRelative == 0 && completedRelative == 0)
			return "";
		else
			return "success";
	}
	public void setRowColor(String rowColor) {
		this.rowColor = rowColor;
	}
	public long getSongsPending() {
		return songsPending;
	}
	public void setSongsPending(long songsPending) {
		this.songsPending = songsPending;
	}
	public long getSongsCompleted() {
		return songsCompleted;
	}
	public void setSongsCompleted(long songsCompleted) {
		this.songsCompleted = songsCompleted;
	}
	public float getPendingRelative() {
		return pendingRelative;
	}
	public void setPendingRelative(float pendingRelative) {
		this.pendingRelative = pendingRelative;
	}
	public float getCompletedRelative() {
		return completedRelative;
	}
	public void setCompletedRelative(float completedRelative) {
		this.completedRelative = completedRelative;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
