package com.friendBook.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.friendBook.model.DBConnection;
import com.friendBook.model.LikeDao;
import com.friendBook.model.PostDao;

import exceptions.LikeException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DBConnection.class, LikeDao.class})
public class LikeTests {
	@Autowired
	LikeDao lDao;
	
	@Test
	public void testCheckIfExistsFalse() throws LikeException {
		assertFalse(lDao.checkIfLikeExistsInDb(5, 5));
	}
	@Test
	public void testCheckIfExistsTrue() throws LikeException {
		assertTrue(lDao.checkIfLikeExistsInDb(99, 3));
	}
	@Test(expected = LikeException.class)
	public void testWrongIdInput() throws LikeException {
		assertTrue(lDao.checkIfLikeExistsInDb(-3, 3));
	}
	@Test(expected = LikeException.class)
	public void testLikeFailDuplicate() throws LikeException {
		assertFalse(lDao.likeAPost(99, 3));
	}
}
