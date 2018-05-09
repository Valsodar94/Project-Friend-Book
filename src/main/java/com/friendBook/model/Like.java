package com.friendBook.model;

import com.friendBook.model.User;

public class Like {
	private Likeable entity;
	private User user;
	public Like(Likeable entity, User user) {
		this.entity = entity;
		this.user = user;
	}
	public Likeable getEntity() {
		return entity;
	}
	public void setEntity(Likeable entity) {
		this.entity = entity;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
