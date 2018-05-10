package com.friendBook.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendBook.model.Comment;
import com.friendBook.model.CommentDAO;
import com.friendBook.model.DBConnection;
import com.friendBook.model.LikeDao;

import exceptions.CommentException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DBConnection.class, CommentDAO.class, LikeDao.class})
public class CommentTests {

	@Autowired
	CommentDAO cDao;
	
	@Test
	public void addCommentSuccess() throws CommentException {
		Comment c = new Comment(0,3,99);
		c.setText("testAnswer");
		cDao.putComment(c);
	}
	@Test(expected = CommentException.class)
	public void failAddComment() throws CommentException {
		Comment c = new Comment(0,3,99);
		cDao.putComment(c);
	}

}
