package com.friendBook.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Comment implements Likeable{
	private int id;
	private LocalDateTime time;
	private User user;
	private List<Like> likes;
	public Comment(int id, LocalDateTime time, User user) {
		super();
		this.id = id;
		this.time = time;
		this.user = user;
		likes = new ArrayList<>();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Like> getLikes() {
		return Collections.unmodifiableList(likes);
	}
}
