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
import org.springframework.stereotype.Component;

import exceptions.CommentException;
import exceptions.PostException;

@Component
public class CommentDAO implements ICommentDAO {
	private static final String COMMENT_POST_SQL = "INSERT INTO comments\r\n"
			+ "(comment_content, comment_user_id, comment_post_id)\r\n" + "VALUES(?, ?, ?);";
	private static final String POST_COMMENTS_SQL = "SELECT * FROM comments WHERE comment_post_id = ?;";

	private final DBConnection db;

	public CommentDAO() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();
	}
	
	
	public List<Comment> extractComments(int postId) throws CommentException {
		List<Comment> postsComments = new LinkedList<>();
		if(postId <= 0) {
			return postsComments;
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(POST_COMMENTS_SQL);
			pstmt.setInt(1, postId);

			ResultSet resultSet = pstmt.executeQuery();
			while (resultSet.next()) {
				Comment comment = new Comment(resultSet.getInt(1), resultSet.getInt(4), postId);
				comment.setText(resultSet.getString(2));
				comment.setTime(LocalDateTime.parse(resultSet.getString(3),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")));
				postsComments.add(comment);
			}
			Collections.sort(postsComments);
			return postsComments;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException("Something went wrong with DB", e);
		}
	}

	@Override
	public boolean putComment(Comment comment) throws CommentException {
		if (comment == null || comment.getText() == null 
				|| comment.getText().length() == 0) {
			return false;
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(COMMENT_POST_SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, comment.getText());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setInt(3, comment.getPostId());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			System.out.println(resultSet.toString());
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException("Something went wrong with DB", e);
		}
		
	}

}
