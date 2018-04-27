package dao;

import java.util.List;

import exceptions.PostException;
import post.Post;

public interface IPostDAO {
	
	boolean publish(Post post) throws PostException;
	
	List<Post> extractPosts(int userId) throws PostException;
	
	void edit() throws PostException;
	
	void delete() throws PostException;

}
