package com.friendBook.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.friendBook.model.DBConnection;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserDao.class, DBConnection.class})
public class UserTests {
	@Autowired
	UserDao uDao;

	@Test(expected = LoginException.class)
	public void testBadUsername() throws LoginException {
		uDao.login("raditass", "123456");
	}
	
	@Test(expected = LoginException.class)
	public void testBadPassword() throws LoginException {
		uDao.login("raditas", "1234567");
	}
	
	@Test
	public void testSucesslogin() throws LoginException {
		assertEquals(12,uDao.login("raditas", "123456"));
	}
	
	@Test
	public void registerSuccess()throws UserException, RegisterException {
		User u = new User(0,"Kolio","123456","kolio@abv.bg");
		assertTrue(uDao.register(u) > 0);
	}
	
	@Test(expected = RegisterException.class)
	public void registerFailDuplicateUsername() throws UserException, RegisterException {
		User u = new User(0,"Kolio","1236","kolio@abv.bg");
		uDao.register(u);
	}
	
	

}
