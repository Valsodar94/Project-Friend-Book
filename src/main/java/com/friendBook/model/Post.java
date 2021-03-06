package com.friendBook.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.friendBook.model.Comment;
import com.friendBook.model.Likeable;

public class Post implements Likeable, Comparable<Post>{
	private int id;
	private String text;
	private String pictureUrl;
	private LocalDateTime time;
	private int userId;
	private List<Comment> comments;
	private int likes;
	private String userUserName;
	private String tags;
	
	public Post(int id, int userId) {
		this.id = id;
		this.text = "";
		this.pictureUrl = "";
		this.time = LocalDateTime.now();
		this.userId = userId;
		this.comments = new ArrayList<>();
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		if(text != null && text.length() > 0) {
			this.text = text;
		}		
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}
	
	public void setPictureUrl(String pictureUrl) {
		if(pictureUrl != null && pictureUrl.length() > 0) {
			this.pictureUrl = pictureUrl;
		}		
	}
	
	public String getTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String text = time.format(formatter);
		return text;
	}
	
	public void setTime(LocalDateTime time) {
		if(time != null) {
			this.time = time;
		}		
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}
	
	public void addComment(Comment comment) {
		if(comment != null) {
			this.comments.add(comment);
		}
	}
	
	public void removeComment(Comment comment) {
		if(comment != null) {
			for(Comment c: comments) {
				if(c.equals(comment)) {
					this.comments.remove(c);
					break;
				}
			}			
		}
	}
	
	public void setLikes(int likes) {
		if(likes>=0)
			this.likes = likes;
	}
	public int getLikes() {
		return likes;
	}
	

	@Override
	public String toString() {
		return "Post [id=" + id + ", text=" + text + ", pictureUrl=" + pictureUrl + ", time=" + time + ", userId="
				+ userId + ", comments=" + comments + ", likes=" + likes + "]";
	}

	@Override
	public int compareTo(Post otherPost) {
		return otherPost.getTime().compareTo(this.getTime());
	}
	
	public String getUserUserName() {
		return userUserName;
	}
	public void setUserUserName(String userUserName) {
		this.userUserName = userUserName;
	}
	
}
