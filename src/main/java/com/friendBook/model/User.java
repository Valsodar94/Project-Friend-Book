package com.friendBook.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import exceptions.UserException;

public class User {
	private static final int MAX_PASSWORD_LENGTH = 20;
	private static final int MIN_PASSWORD_LENGTH = 5;
	private static final int MAX_USERNAME_LENGTH = 15;
	private static final int MIN_USERNAME_LENGTH = 3;
	private int id;
	private String username;
	private String password;
	private String email;
	private Set<User> followedUsers;
	private List<Post> posts;
	
	public User(int id, String username, String password, String email) throws UserException {
		this.id = id;
		setUsername(username);
		this.password = password;
		this.email = email;
		this.followedUsers = new HashSet<>();
		this.posts = new ArrayList<>();		
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Set<User> getFollowedUsers() {
		return Collections.unmodifiableSet(followedUsers);
	}
	
	public void addFollowedUser(User user) {
		if(user != null) {
			this.followedUsers.add(user);
		}
	}
	
	public void removeFollowedUser(User user) {
		if(user != null) {
			for(Iterator<User> it = followedUsers.iterator(); it.hasNext();) {
				if(it.equals(user)) {
					this.followedUsers.remove(user);
					break;
				}
			}			
		}
	}

	public List<Post> getPosts() {
		return Collections.unmodifiableList(posts);
	}

	public void addPost(Post post) {
		if(post != null) {
			this.posts.add(post);
		}
	}
	
	public void removePost(Post post) {
		if(post != null) {
			for(Post p: posts) {
				if(p.equals(post)) {
					posts.remove(post);
					break;
				}
			}
		}
	}
	
	public void setUsername(String username) throws UserException {
		if(username!=null) {
			if(username.trim().length()>=MIN_USERNAME_LENGTH) {
				if(username.trim().length()<=MAX_USERNAME_LENGTH) {
					this.username = username;
				} else 
					throw new UserException("The username is above the maximum length");
			}else
				throw new UserException("The username is below the minimum length");
		} else
			throw new UserException("The username is null");

		
	}
	public void setPassword(String password) throws UserException {
		if(password!=null) {
			if(password.trim().length()>=MIN_PASSWORD_LENGTH) {
				if(password.trim().length()<=MAX_PASSWORD_LENGTH) {
					this.password = password;
				} else
					throw new UserException("The password is above the maximum length");
			} else
				throw new UserException("The password is below the minimum length");
		} else
			throw new UserException("The password is null");
	}
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) throws UserException {
		if(email != null) {
			if(email.matches("^(.+)@(.+)$")) {
				this.email = email;
			} else {
				throw new UserException("Invalid email format");
			}			
		} else {
			throw new UserException("The email is null");
		}
	}
}
