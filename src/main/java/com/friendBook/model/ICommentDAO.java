package com.friendBook.model;

import exceptions.CommentException;

public interface ICommentDAO {
	boolean putComment(Comment comment) throws CommentException;
}
