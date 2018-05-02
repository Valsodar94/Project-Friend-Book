package com.friendBook.model;

public class CommentAnswer extends Comment {
	
	private int commentId;

	public CommentAnswer(int id, int userId, int postId, int commentId) {
		super(id, userId, postId);
		this.setCommentId(commentId);
	}
	
	public int getCommentId() {
		return commentId;
	}
	
	public void setCommentId(int commentId) {
		if(commentId > 0) {
			this.commentId = commentId;
		}		
	}
	
	@Override
	public int compareTo(Comment otherComment) {
		return this.getTime().compareTo(otherComment.getTime());
	}

}
