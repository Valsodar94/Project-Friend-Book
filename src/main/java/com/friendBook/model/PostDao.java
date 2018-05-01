package com.friendBook.model;

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

import exceptions.LikeException;
import exceptions.PostException;

@Component
public class PostDao implements IPostDAO {
	private static final String EDIT_POST_SQL = "SELECT * FROM users WHERE user_name=? and user_pass = sha1(?)";
	private static final String ADD_POST_SQL = "INSERT INTO posts (post_text, post_picture, post_time, post_user_id) "
			+ "VALUES (?, ?, localtime(), ?);";
	private static final String EXTRACT_POSTS_SQL = "SELECT * FROM posts WHERE post_user_id=?";
	private static final String GET_POST_SQL = "SELECT * FROM posts WHERE post_id=?";

	private final DBConnection db;
	
	@Autowired
	private LikeDao likeDao;

	public PostDao() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();
	}

	public Post getPostById(int postId) throws PostException, LikeException {
		if (postId <= 0) {
			return null;
		}

		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(GET_POST_SQL);
			pstmt.setInt(1, postId);

			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			Post post = new Post(resultSet.getInt(1), resultSet.getInt(5));
			post.setText(resultSet.getString(2));
			post.setPictureUrl(resultSet.getString(3));
			post.setTime(LocalDateTime.parse(resultSet.getString(4), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
			List<Integer> likedPostUserIds = new LinkedList<>(likeDao.getUsersIdForLikedPost(postId));
			post.setLikes(likedPostUserIds.size());
			return post;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with DB", e);
		}
	}

	public boolean publish(Post post) throws PostException {
		if ((post.getText() == null || post.getText().length() == 0)
				&& (post.getPictureUrl() == null || post.getPictureUrl().length() == 0)) {
			return false;
		}
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(ADD_POST_SQL, Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, post.getText());
			pstmt.setString(2, post.getPictureUrl());
			pstmt.setInt(3, post.getUserId());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			return resultSet.next();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with DB", e);
		}
	}

	public List<Post> extractPosts(int userId) throws PostException {
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(EXTRACT_POSTS_SQL);
			pstmt.setInt(1, userId);
			List<Post> usersPosts = new LinkedList<>();

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				int postId = resultSet.getInt(1);
				Post post = new Post(postId, userId);
				post.setText(resultSet.getString(2));
				post.setPictureUrl(resultSet.getString(3));
				post.setTime(LocalDateTime.parse(resultSet.getString(4),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				List<Integer> likedPostUserIds = new LinkedList<>(likeDao.getUsersIdForLikedPost(postId));
				post.setLikes(likedPostUserIds.size());
				usersPosts.add(post);
			}
			Collections.sort(usersPosts);
			return usersPosts;
		} catch (SQLException | LikeException e) {
			e.printStackTrace();
			throw new PostException("Something went wrong with DB", e);
		}
	}

	public void delete() throws PostException {

	}

	public void edit() throws PostException {

	}

}
