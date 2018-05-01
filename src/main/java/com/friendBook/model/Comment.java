package com.friendBook.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Comment implements Likeable, Comparable<Comment>{
	
	private int id;
	private String text;
	private LocalDateTime time;
	private int userId;
	private int postId;
	private List<CommentAnswer> answers;
	private Set<Like> likes;
	
	public Comment(int id, int userId, int postId) {
		this.id = id;
		this.text = "";
		this.time = LocalDateTime.now();
		this.userId = userId;
		this.postId = postId;
		this.answers = new ArrayList();
		this.likes = new HashSet();
	}

	public int getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getTime() {
		return time;
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

	public List<CommentAnswer> getComments() {
		return Collections.unmodifiableList(answers);
	}
	
	public void addComment(CommentAnswer answer) {
		if(answer != null) {
			this.answers.add(answer);
		}
	}
	
	public void removeComment(CommentAnswer answer) {
		if(answer != null) {
			for(CommentAnswer a: answers) {
				if(a.equals(answer)) {
					this.answers.remove(a);
					break;
				}
			}			
		}
	}
	
	public Set<Like> getLikes() {
		return Collections.unmodifiableSet(likes);
	}
	
	public void addLike(Like like) {
		if(like != null) {
			this.likes.add(like);
		}
	}
	
	public void removeLike(Like like) {
		if(like != null) {
			for(Iterator<Like> it = likes.iterator(); it.hasNext();) {
				if(it.equals(like)) {
					this.likes.remove(like);
					break;
				}
			}			
		}
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
