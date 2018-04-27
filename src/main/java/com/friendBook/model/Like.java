package com.friendBook.model;

public class Like {
	private Likeable entity;
	private User user;
	public Like(Likeable entity, User user) {
		this.entity = entity;
		this.user = user;
	}
}
