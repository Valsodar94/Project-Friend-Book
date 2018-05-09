package com.friendBook.model;

import java.util.List;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

public interface IUserDao {
	User login(String username, String password) throws LoginException;
	
	int register(User u) throws RegisterException;
	
	boolean checkIfUsernameExistsInDB(String username) throws UserException;

	boolean checkIfEmailExistsInDB(String email) throws UserException;

	User getUserById(int id) throws UserException;

	List<User> getUsersByString(String name) throws UserException;

	boolean follow(int followerID, int followedID) throws UserException;

	boolean unfollow(int followerID, int followedID) throws UserException;

	List<User> getAllFollowedUsers(int id) throws UserException;

	List<User> getAllFollowers(int id) throws UserException;

	boolean checkIfFollowExistsInDb(int followerId, int followedId) throws UserException;

	boolean checkIfUserExistsInDB(Integer id) throws UserException;

	boolean checkIfAccountVerified(String username) throws UserException;

	boolean checkConfirmationCode(int confirmationCode, String username) throws UserException;

	boolean verifyAccount(String username) throws UserException;

	boolean resetPassword(String email, String newPassword) throws UserException;

	boolean editProfile(int id, String pass, String email) throws UserException;

	boolean deleteProfile(int userId) throws UserException;

	boolean checkifAdmin(int userId) throws UserException;
}
