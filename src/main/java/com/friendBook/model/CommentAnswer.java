package com.friendBook.model;

public class CommentAnswer extends Comment {
	
	private int commentId;

	public CommentAnswer(int id, int userId, int postId, int commentId) {
		super(id, userId, postId);
		this.commentId = commentId;
	}
	
	public int getCommentId() {
		return commentId;
	}

}
