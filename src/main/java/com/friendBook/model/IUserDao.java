package com.friendBook.model;

import exceptions.LoginException;
import exceptions.RegisterException;

public interface IUserDao {
	User login(String username, String password) throws LoginException;
	int register(User u) throws RegisterException;
}
