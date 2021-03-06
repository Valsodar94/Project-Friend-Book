package com.friendBook.model;

import static org.hamcrest.CoreMatchers.containsString;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.friendBook.model.DBConnection;
import com.friendBook.model.LikeDao;

import exceptions.CommentException;
import exceptions.LikeException;
import exceptions.PostException;

@Component
public class PostDao implements IPostDao {
//constants
	private static final String INVALID_POST = "No such post";
	private static final String ERROR_MESSAGE_FOR_NULL_POST = "Post is null";
	private static final String ERROR_MESSAGE_FOR_INVALID_ID = "Invalid id";
	private static final String DB_ERROR_MESSAGE = "Something went wrong with the database!";
//	DB statements
	private static final String ADD_POST_SQL = "INSERT INTO posts (post_text, post_picture, post_time, post_user_id, post_tags) "
			+ "VALUES (?, ?, localtime(), ?, ?)";
	private static final String EXTRACT_POSTS_SQL = "select u.user_name, p.post_id, p.post_text, p.post_picture, p.post_time, p.post_user_id, p.is_deleted, p.post_tags\r\n" + 
						"from users u join posts p\r\n" + 
						"on(u.user_id = p.post_user_id)\r\n" + 
						"where u.user_id = ?";
	private static final String GET_POST_SQL = "SELECT * FROM posts WHERE post_id=?";
	private static final String DELETE_POST_BY_ID = "update posts\r\n" + 
			"set is_deleted =1\r\n" + 
			"where post_id = ?";
	private static final String DELETE_COMMENTS_BY_POST_ID = "update comments\r\n" + 
			"set is_deleted =1\r\n" + 
			"where comment_post_id = ?";
	private static final String SEARCH_POSTS_BY_TAG = "select u.user_name, p.post_id, p.post_text, p.post_picture, p.post_time, p.post_user_id, p.is_deleted, p.post_tags\r\n" + 
			"from users u join posts p\r\n" + 
			"on(u.user_id = p.post_user_id)\r\n" + 
			"where p.post_tags like ?";

	@Autowired
	private DBConnection db;
	
	@Autowired
	private LikeDao likeDao;


	@Override
	public Post getPostById(int postId) throws PostException {
		if (postId <= 0) {
			throw new PostException(ERROR_MESSAGE_FOR_INVALID_ID);
		}

		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(GET_POST_SQL);
			pstmt.setInt(1, postId);

			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			if(resultSet.getBoolean("is_deleted")){
				throw new PostException(INVALID_POST);
			}
			Post post = new Post(resultSet.getInt(1), resultSet.getInt(5));
			post.setText(resultSet.getString(2));
			post.setPictureUrl(resultSet.getString(3));
			post.setTime(LocalDateTime.parse(resultSet.getString(4), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
			List<Integer> likedPostUserIds = new LinkedList<>(likeDao.getUsersIdForLikedPost(postId));
			post.setLikes(likedPostUserIds.size());
			return post;
		} catch (SQLException | LikeException e) {
			e.printStackTrace();
			throw new PostException(DB_ERROR_MESSAGE, e);
		}
	}

	@Override
	public boolean publish(Post post) throws PostException {
		if (post == null) {
			throw new PostException(ERROR_MESSAGE_FOR_NULL_POST);
		}
		if ((post.getText() == null || post.getText().length() == 0)
				&& (post.getPictureUrl() == null || post.getPictureUrl().length() == 0)) {
			return false;
		}
		System.out.println("[DEBUG] PostDAO .publish " + post.getText());
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(ADD_POST_SQL, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, post.getText());
			pstmt.setString(2, post.getPictureUrl());
			pstmt.setInt(3, post.getUserId());
			pstmt.setString(4, post.getTags());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			return resultSet.next();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException(DB_ERROR_MESSAGE, e);
		}
	}

	@Override
	public List<Post> extractPosts(int userId) throws PostException {
		if (userId <= 0) {
			throw new PostException(ERROR_MESSAGE_FOR_INVALID_ID);
		}
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(EXTRACT_POSTS_SQL);
			pstmt.setInt(1, userId);
			List<Post> usersPosts = new LinkedList<>();

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getBoolean("is_deleted")) {
					continue;
				}
				int postId = resultSet.getInt(2);
				Post post = new Post(postId, userId);
				post.setTags(resultSet.getString("post_tags"));
				post.setText(resultSet.getString(3));
				post.setPictureUrl(resultSet.getString(4));
				post.setTime(LocalDateTime.parse(resultSet.getString(5),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				List<Integer> likedPostUserIds = new LinkedList<>(likeDao.getUsersIdForLikedPost(postId));
				post.setLikes(likedPostUserIds.size());
				post.setUserId(resultSet.getInt("post_user_id"));
				post.setUserUserName(resultSet.getString(1));
				usersPosts.add(post);
			}
			Collections.sort(usersPosts);
			return usersPosts;
		} catch (SQLException | LikeException e) {
			e.printStackTrace();
			throw new PostException(DB_ERROR_MESSAGE, e);
		}
	}
	@Override
	public List<Post> extractPostsByTag(String tag) throws PostException {
		if(tag == null || tag.length() < 1) {
			return null;
		}
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(SEARCH_POSTS_BY_TAG);
			pstmt.setString(1, "%"+tag+"%");
			List<Post> usersPosts = new LinkedList<>();
	
			ResultSet resultSet = pstmt.executeQuery();
			while(resultSet.next()) {
				
				if(resultSet.getBoolean("is_deleted")) {
					continue;
				}
				if(checkIfTagMatchAnyOfPostTags(resultSet.getString("post_tags"), tag)) {	
					int postId = resultSet.getInt(2);
					Post post = new Post(postId, resultSet.getInt("post_user_id"));
					post.setTags(resultSet.getString("post_tags"));
					post.setText(resultSet.getString(3));
					post.setPictureUrl(resultSet.getString(4));
					post.setTime(LocalDateTime.parse(resultSet.getString(5),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
					List<Integer> likedPostUserIds = new LinkedList<>(likeDao.getUsersIdForLikedPost(postId));
					post.setLikes(likedPostUserIds.size());
					post.setUserId(resultSet.getInt("post_user_id"));
					post.setUserUserName(resultSet.getString(1));
					usersPosts.add(post);
				}
			}
			Collections.sort(usersPosts);
			return usersPosts;
		}
		catch(SQLException | LikeException e) {
			e.printStackTrace();
			throw new PostException(DB_ERROR_MESSAGE, e);
		}
		
	}
	@Override
	public boolean checkIfTagMatchAnyOfPostTags(String postTags, String tag) {
		if(postTags == null || postTags.length() < 1) {
			return false;
		}
		String[] singlePostTags = postTags.split(" ");
		for(String tag1: singlePostTags) {
			if(tag.equalsIgnoreCase(tag1))
				return true;
		}
		return false;
	}

	@Override
	public boolean deletePost(int postId) throws PostException {
		if(postId > 0) {
			PreparedStatement pstmt;
			Connection con = db.getConnection();
			try {
				con.setAutoCommit(false);
				pstmt = con.prepareStatement(DELETE_POST_BY_ID);
				pstmt.setInt(1, postId);
				int updatedRows = pstmt.executeUpdate();
				pstmt = con.prepareStatement(DELETE_COMMENTS_BY_POST_ID);
				pstmt.setInt(1, postId);
				pstmt.executeUpdate();
				con.commit();
				if(updatedRows > 0)
					return true;
				else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new PostException(DB_ERROR_MESSAGE, e);
				}
				throw new PostException(DB_ERROR_MESSAGE, e);
			}
			finally {
				try {
					con.setAutoCommit(true);
				} catch (SQLException e) {
					e.printStackTrace();
					throw new PostException(DB_ERROR_MESSAGE, e);
				}
			}

		}
		else {
			throw new PostException(ERROR_MESSAGE_FOR_INVALID_ID);
		}
	}
	


}
