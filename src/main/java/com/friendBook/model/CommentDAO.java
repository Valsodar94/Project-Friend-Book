package com.friendBook.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import org.springframework.stereotype.Component;

import exceptions.CommentException;
import exceptions.PostException;

@Component
public class CommentDAO implements ICommentDAO {
	private static final String COMMENT_POST_SQL = "INSERT INTO comments\r\n"
			+ "(comment_content, comment_user_id, comment_post_id)\r\n" + "VALUES(?, ?, ?);";

	private final DBConnection db;

	public CommentDAO() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();
	}

	@Override
	public int putComment(Comment comment) throws CommentException {
		if (comment.getText() == null || comment.getText().length() == 0) {
			return 0;
		}
		
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(COMMENT_POST_SQL, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, comment.getText());
			pstmt.setInt(2, comment.getUserId());
			pstmt.setInt(3, comment.getPostId());
			pstmt.executeUpdate();

			ResultSet resultSet = pstmt.getGeneratedKeys();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CommentException("Something went wrong with DB", e);
		}
		
	}

}
