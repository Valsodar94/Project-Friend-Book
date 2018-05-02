package com.friendBook.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import exceptions.LikeException;
import exceptions.UserException;

@Component
public class LikeDao {

	private static final String LIKE_A_POST = "INSERT INTO likes_post VALUES (?, ?)";
	private static final String LIKE_A_COMMENT = "INSERT INTO likes_comment VALUES (?, ?)";
	private static final String DISLIKE_A_COMMENT = "DELETE FROM likes_comment WHERE comment_id = ? AND user_id = ?";
	private static final String CHECK_IF_LIKE_COMMENT_ALREADY_EXISTS = "SELECT * FROM likes_comment WHERE comment_id = ? AND user_id =?";
	private static final String CHECK_IF_LIKE_ALREADY_EXISTS = "SELECT * FROM likes_post WHERE post_id = ? AND user_id =?";
	private static final String DISLIKE_A_POST = "DELETE FROM likes_post WHERE post_id = ? AND user_id = ?";
	private static final String EXTRACT_LIKES_FOR_POST = "SELECT * FROM likes_post WHERE post_id = ?";
	private static final String EXTRACT_LIKES_FOR_COMMENT = "SELECT * FROM likes_comment WHERE comment_id = ?";
	private final DBConnection db;
	
	
	public LikeDao() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();	
	}
	
	public boolean checkIfLikeExistsInDb(int postId,int userId) throws LikeException {
		if(userId <=0 || postId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(CHECK_IF_LIKE_ALREADY_EXISTS);
			pstmt.setInt(1, postId);
			pstmt.setInt(2, userId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Something went wrong with DB", e);
		}

	}
	
	public boolean checkIfLikeCommentExistsInDb(int commentId,int userId) throws LikeException {
		if(userId <=0 || commentId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(CHECK_IF_LIKE_COMMENT_ALREADY_EXISTS);
			pstmt.setInt(1, commentId);
			pstmt.setInt(2, userId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Something went wrong with DB", e);
		}

	}
	
	public boolean likeAPost(int postId, int userId) throws LikeException {
		if(userId <=0 || postId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(LIKE_A_POST);
			pstmt.setInt(1, postId);
			pstmt.setInt(2, userId);
			int addedRows = pstmt.executeUpdate();
			if(addedRows>0)
				return true;
			else
				throw new LikeException("Like failed");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Someting went wrong with DB", e);
		}
	}
	
	public boolean likeAComment(int commentId, int userId) throws LikeException {
		if(userId <=0 || commentId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(LIKE_A_COMMENT);
			pstmt.setInt(1, commentId);
			pstmt.setInt(2, userId);
			int addedRows = pstmt.executeUpdate();
			if(addedRows>0)
				return true;
			else
				throw new LikeException("Like failed");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Someting went wrong with DB", e);
		}
	}
	
	public boolean dislikeAPost(int postId, int userId) throws LikeException {
		if(userId <=0 || postId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(DISLIKE_A_POST);
			pstmt.setInt(1, postId);
			pstmt.setInt(2, userId);
			int addedRows = pstmt.executeUpdate();
			if(addedRows>0)
				return true;
			else
				throw new LikeException("Like failed");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Someting went wrong with DB", e);
		}
	}
	
	public boolean dislikeAComment(int commentId, int userId) throws LikeException {
		if(userId <=0 || commentId<=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(DISLIKE_A_COMMENT);
			pstmt.setInt(1, commentId);
			pstmt.setInt(2, userId);
			int addedRows = pstmt.executeUpdate();
			if(addedRows>0)
				return true;
			else
				throw new LikeException("Like failed");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Someting went wrong with DB", e);
		}
	}
	
	
	public List<Integer> getUsersIdForLikedPost(int postId) throws LikeException {
		if(postId <=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(EXTRACT_LIKES_FOR_POST);
			pstmt.setInt(1, postId);
			ResultSet rs = pstmt.executeQuery();
			List<Integer> userIds = new LinkedList<>();
			while(rs.next()) {
				userIds.add(rs.getInt(2));
			}
			return userIds;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Something went wrong with DB", e);
		}

	}
	public List<Integer> getUsersIdForLikedComment(int commentId) throws LikeException {
		if(commentId <=0)
			throw new LikeException("Invalid id");
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(EXTRACT_LIKES_FOR_COMMENT);
			pstmt.setInt(1, commentId);
			ResultSet rs = pstmt.executeQuery();
			List<Integer> userIds = new LinkedList<>();
			while(rs.next()) {
				userIds.add(rs.getInt(2));
			}
			return userIds;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LikeException("Something went wrong with DB", e);
		}
	}
	
}
