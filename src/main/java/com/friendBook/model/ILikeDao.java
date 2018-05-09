package com.friendBook.model;

import java.util.List;

import exceptions.LikeException;

public interface ILikeDao {

	boolean checkIfLikeExistsInDb(int postId, int userId) throws LikeException;

	boolean checkIfLikeCommentExistsInDb(int commentId, int userId) throws LikeException;

	boolean likeAPost(int postId, int userId) throws LikeException;

	boolean likeAComment(int commentId, int userId) throws LikeException;

	boolean dislikeAPost(int postId, int userId) throws LikeException;

	boolean dislikeAComment(int commentId, int userId) throws LikeException;

	List<Integer> getUsersIdForLikedPost(int postId) throws LikeException;

	List<Integer> getUsersIdForLikedComment(int commentId) throws LikeException;

}