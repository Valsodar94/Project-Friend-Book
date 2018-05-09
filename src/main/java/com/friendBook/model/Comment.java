package com.friendBook.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.friendBook.model.Likeable;

public class Comment implements Likeable, Comparable<Comment>{
	
	private int id;
	private String text;
	private LocalDateTime time;
	private int userId;
	private int postId;
	private List<CommentAnswer> answers;
	private int likes;
	private String authorName;
	
	public Comment(int id, int userId, int postId) {
		this.id = id;
		this.text = "";
		this.time = LocalDateTime.now();
		this.userId = userId;
		this.postId = postId;
		this.answers = new ArrayList<>();
		this.likes = 0;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String text = time.format(formatter);
		return text;
	}

	public int getUserId() {
		return userId;
	}
	
	public int getPostId() {
		return postId;
	}
	
	public void setText(String text) {
		if(text != null & text.length() > 0) {
			this.text = text;
		}		
	}
	
	public void setTime(LocalDateTime time) {
		if(time != null) {
			this.time = time;
		}	
	}

	public List<CommentAnswer> getAnswers() {
		return Collections.unmodifiableList(answers);
	}
	
	public void setAnswers(List<CommentAnswer> answers) {
		this.answers = answers;
	}

	public void addAnswer(CommentAnswer answer) {
		if(answer != null) {
			this.answers.add(answer);
		}
	}
	
	public void removeAnswer(CommentAnswer answer) {
		if(answer != null) {
			for(CommentAnswer a: answers) {
				if(a.equals(answer)) {
					this.answers.remove(a);
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
		return "Comment [id=" + id + ", text=" + text + ", time=" + time 
				+ ", userId=" + userId + ", postId=" + postId
				+ ", answers=" + answers + ", likes=" + likes + "]";
	}

	@Override
	public int compareTo(Comment otherComment) {
		return otherComment.getTime().compareTo(this.getTime());
	}
	
}
