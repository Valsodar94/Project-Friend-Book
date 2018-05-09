package com.friendBook.model;

import java.util.List;

import exceptions.CommentException;

public interface ICommentDao {

	List<Comment> extractComments(int postId) throws CommentException;

	List<CommentAnswer> extractAnswers(int commentId) throws CommentException;

	boolean putComment(Comment comment) throws CommentException;

	boolean answerComment(CommentAnswer answer) throws CommentException;

	Comment getCommentById(int commentId) throws CommentException;

	boolean deleteAnwer(int answerId) throws CommentException;

	boolean deleteComment(int commentId) throws CommentException;

}