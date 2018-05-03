package com.friendBook.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.sql.PreparedStatement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import exceptions.CommentException;
import exceptions.LikeException;
import exceptions.PostException;

@Component
public class CommentDAO implements ICommentDAO {

//	constants
	private static final String ERROR_MESSAGE_FOR_ANSWER_INPUT = "Invalid answer input";
	private static final String ERROR_FOR_COMMENT_INPUT = "Invalid comment input";
	private static final String ERROR_MESSAGE_FOR_INVALID_ID = "Invalid id";
	private static final String DB_ERROR_MESSAGE = "Something went wrong with the database!";

// DB statements
	private static final String COMMENT_POST_SQL = "INSERT INTO comments\r\n"
			+ "(comment_content, comment_user_id, comment_post_id)\r\n" 
			+ "VALUES(?, ?, ?);";
	private static final String POST_COMMENTS_SQL = "SELECT * FROM comments WHERE comment_post_id = ?;";
	private static final String ANSWER_COMMENT_SQL = "INSERT INTO comments\r\n" 
			+ "(comment_content, comment_user_id, comment_answer,comment_post_id)\r\n" 
			+ "VALUES(?, ?, ?, ?);";
	private static final String COMMENT_ANSWERS_SQL = "SELECT * FROM comments WHERE comment_answer = ?;";
	private static final String GET_COMMENT_SQL = "SELECT * FROM comments WHERE comment_id=?";
	
	@Autowired
	private DBConnection db;
	@Autowired
	private LikeDao likeDao;

	
	public List<Comment> extractComments(int postId) throws CommentException {
		List<Comment> postsComments = new LinkedList<>();
		if(postId <= 0) {
			throw new CommentException(ERROR_MESSAGE_FOR_INVALID_ID);
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(POST_COMMENTS_SQL);
			pstmt.setInt(1, postId);

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				if(resultSet.getInt(5) != 0) {
					continue;
				}
				int commentId = resultSet.getInt(1);
				Comment comment = new Comment(commentId, resultSet.getInt(4), postId);
				comment.setText(resultSet.getString(2));
				comment.setTime(LocalDateTime.parse(resultSet.getString(3),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				List<Integer> likedCommentUserIds = new LinkedList<>(likeDao.getUsersIdForLikedComment(commentId));
				comment.setLikes(likedCommentUserIds.size());
				comment.setAnswers(extractAnswers(commentId));
				postsComments.add(comment);
			}
			Collections.sort(postsComments);
			return postsComments;
		} catch (SQLException | LikeException e) {
			e.printStackTrace();
			throw new CommentException(DB_ERROR_MESSAGE, e);
		}
	}
	
	public List<CommentAnswer> extractAnswers(int commentId) throws CommentException {
		List<CommentAnswer> comentAnswers = new LinkedList<>();
		if(commentId <= 0) 
			throw new CommentException(ERROR_MESSAGE_FOR_INVALID_ID);
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(COMMENT_ANSWERS_SQL);
			pstmt.setInt(1, commentId);

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				CommentAnswer answer = new CommentAnswer(resultSet.getInt(1), 
						resultSet.getInt(4), resultSet.getInt(6),commentId);
				answer.setText(resultSet.getString(2));
				answer.setTime(LocalDateTime.parse(resultSet.getString(3),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				comentAnswers.add(answer);
			}
			Collections.sort(comentAnswers);
			return comentAnswers;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException(DB_ERROR_MESSAGE, e);
		}
	}

	@Override
	public boolean putComment(Comment comment) throws CommentException {
		if (comment == null || comment.getText() == null 
				|| comment.getText().length() == 0) {
			throw new CommentException(ERROR_FOR_COMMENT_INPUT);
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(COMMENT_POST_SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, comment.getText());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setInt(3, comment.getPostId());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException(DB_ERROR_MESSAGE, e);
		}
		
	}
	
	public boolean answerComment(CommentAnswer answer) throws CommentException {
		if (answer == null || answer.getText() == null 
				|| answer.getText().length() == 0) {
			throw new CommentException(ERROR_MESSAGE_FOR_ANSWER_INPUT);
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(ANSWER_COMMENT_SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, answer.getText());
			pstmt.setInt(2, answer.getUserId());
			pstmt.setInt(3, answer.getCommentId());
			pstmt.setInt(4, answer.getPostId());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException(DB_ERROR_MESSAGE, e);
		}
		
	}

	public Comment getCommentById(int commentId) throws CommentException{
		if(commentId <= 0) {
			throw new CommentException(ERROR_MESSAGE_FOR_INVALID_ID);
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(GET_COMMENT_SQL);
			pstmt.setInt(1, commentId);
			ResultSet resultSet = pstmt.executeQuery();
			resultSet.next();
			
			Comment comment = new Comment(resultSet.getInt(1), resultSet.getInt(4), resultSet.getInt(6));
			comment.setText(resultSet.getString(2));
			comment.setTime(LocalDateTime.parse(resultSet.getString(3),
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
			
			return comment;
		}catch (SQLException e){
			e.printStackTrace();
			throw new CommentException(DB_ERROR_MESSAGE, e);
		}
	}						

}
