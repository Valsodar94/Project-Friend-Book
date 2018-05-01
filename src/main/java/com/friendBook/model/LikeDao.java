package com.friendBook.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import exceptions.LikeException;
import exceptions.UserException;

@Component
public class LikeDao {

	private static final String LIKE_A_POST = "INSERT INTO likes_post VALUES (?, ?)";
	private static final String CHECK_IF_LIKE_ALREADY_EXISTS = "SELECT * FROM likes_post WHERE post_id = ? AND user_id =?";
	private static final String DISLIKE_A_POST = "DELETE FROM likes_post WHERE post_id = ? AND user_id = ?";
	private final DBConnection db;
	
	
	public LikeDao() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();	
	}
	
	public boolean checkIfLikeExistsInDb(int postId,int userId) throws LikeException {
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
	
	public boolean likeAPost(int postId, int userId) throws LikeException {
		
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
	
	public boolean dislikeAPost(int postId, int userId) throws LikeException {
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
	
	
	
	
	
	
	
}