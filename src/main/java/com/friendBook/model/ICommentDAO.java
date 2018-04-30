package com.friendBook.model;

import exceptions.CommentException;

public interface ICommentDAO {
	int putComment(Comment comment) throws CommentException;
}
