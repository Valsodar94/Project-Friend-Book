package com.friendBook.model;

import java.util.List;

import exceptions.PostException;

public interface IPostDao {

	Post getPostById(int postId) throws PostException;

	boolean publish(Post post) throws PostException;

	List<Post> extractPosts(int userId) throws PostException;

	List<Post> extractPostsByTag(String tag) throws PostException;

	boolean checkIfTagMatchAnyOfPostTags(String postTags, String tag);

	void delete() throws PostException;

	void edit() throws PostException;

	boolean deletePost(int postId) throws PostException;

}