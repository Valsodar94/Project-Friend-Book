package com.friendBook.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendBook.model.DBConnection;
import com.friendBook.model.LikeDao;
import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.UserDao;

import exceptions.PostException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PostDao.class, DBConnection.class, LikeDao.class})
public class PostTests {

	@Autowired
	PostDao pDao;
	
	@Test(expected = PostException.class)
	public void testWrongIdInput() throws PostException {
		pDao.getPostById(-2);
	}
	
	@Test()
	public void publishFailNoTextOrPicture() throws PostException {
		Post post = new Post(0, 3);
		assertFalse(pDao.publish(post));
	}
	@Test()
	public void testExtractingPostsByTag() throws PostException {
		List<Post> postList = new LinkedList<Post>(pDao.extractPostsByTag("Ra"));
		assertTrue(postList.size()==0);
	}
	@Test()
	public void testStringSplit() {
		boolean b = pDao.checkIfTagMatchAnyOfPostTags("Ko staa be", "be");
		assertTrue(b);
	}

}
