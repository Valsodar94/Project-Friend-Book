package com.friendBook.model;

import exceptions.LoginException;
import exceptions.RegisterException;

public interface IUserDao {
	int login(String username, String password) throws LoginException;
	int register(User u) throws RegisterException;
}
