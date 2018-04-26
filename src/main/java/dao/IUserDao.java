package dao;

import exceptions.LoginException;
import exceptions.RegisterException;
import user.User;

public interface IUserDao {
	int login(String username, String password) throws LoginException;
	int register(User u) throws RegisterException;
}
