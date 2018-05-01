package com.friendBook.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import exceptions.LikeException;
import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;
@Component
public class UserDao implements IUserDao{
	private static final String INSERT_INTO_FOLLOWED_USERS = "INSERT INTO followed_users VALUES (?, ?)";
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE user_name=? and user_pass = sha1(?)";
	private static final String ADD_USER_SQL = "INSERT INTO users VALUES (null, ?, sha1(?), ?)";
	private static final String CHECK_FOR_USERNAME = "SELECT * FROM users WHERE user_name=?";
	private static final String CHECK_FOR_EMAIL = "SELECT * FROM users WHERE user_email=?";
	private static final String SELECT_USER = "SELECT * FROM users WHERE user_id=?;";
	private static final String GET_USERS_BY_STRING = "SELECT * FROM users WHERE user_name like ?";
	private static final String REMOVE_FROM_FOLLOWED_USERS = "DELETE FROM followed_users WHERE user_id = ? AND followed_user_id = ?";
	private static final String GET_FOLLOWED_USERS = "SELECT * FROM followed_users WHERE user_id = ?";
	private static final String CHECK_IF_FOLLOW_ALREADY_EXISTS = "SELECT * FROM followed_users WHERE user_id = ? AND followed_user_id =?";
	private static final String CHECK_IF_USER_EXISTS = "SELECT * FROM users WHERE user_id=?";

	private final DBConnection db;
	
	public UserDao() throws ClassNotFoundException, SQLException {
		db = DBConnection.getInstance();
	}
	
	@Override
	public int login(String username, String password) throws LoginException {
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(LOGIN_USER_SQL);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}
			
			throw new LoginException("No such user!");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LoginException("No such database!", e);
		} 
	}
	
	@Override
	public int register(User u) throws RegisterException {
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(ADD_USER_SQL, Statement.RETURN_GENERATED_KEYS);			
			
			pstmt.setString(1, u.getUsername());
			pstmt.setString(2, u.getPassword());			
			pstmt.setString(3, u.getEmail());			
			pstmt.executeUpdate();
			
			ResultSet resultSet = pstmt.getGeneratedKeys();
			resultSet.next();		
			return resultSet.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RegisterException("Something went wrong with DB", e);
		} 
	}
	
	public boolean checkIfUsernameExistsInDB(String username) throws SQLException {
		PreparedStatement pstmt = db.getConnection()
				.prepareStatement(CHECK_FOR_USERNAME);
			pstmt.setString(1, username);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) 
				return true;
			 else
				return false;
	}
	
	public boolean checkIfEmailExistsInDB(String email) throws SQLException {
		PreparedStatement pstmt = db.getConnection()
				.prepareStatement(CHECK_FOR_EMAIL);
			pstmt.setString(1, email);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next()) 
				return true;
			 else
				return false;
	}
	public User getUserById(int id) throws UserException {
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(SELECT_USER);
			pstmt.setInt(1, id);
			
			ResultSet resultSet = pstmt.executeQuery();
			String username = resultSet.getString(2); 
			String password = resultSet.getString(3); 
			String email = resultSet.getString(4); 
			return new User(id,username,password,email);
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new UserException("Something went wrong with DB");
		}
	}
	public List<User> getUsersByString(String name) throws UserException{
		PreparedStatement pstmt;
		try {
			pstmt = db.getConnection().prepareStatement(GET_USERS_BY_STRING);
			pstmt.setString(1, "%"+name+"%");
			ResultSet resultSet = pstmt.executeQuery();
			List<User> users = new LinkedList<>();
			while(resultSet.next()) {
				int id = resultSet.getInt(1);
				String username = resultSet.getString(2);
				String password = resultSet.getString(3);
				String email = resultSet.getString(4);
				User u = new User(id,username,password, email);
				users.add(u);
			}
			return users;
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new UserException("Something went wrong with DB");
		}
	}
	public boolean follow(int followerID, int followedID) throws UserException {
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(INSERT_INTO_FOLLOWED_USERS);
			pstmt.setInt(1, followerID);
			pstmt.setInt(2, followedID);
			int addedRows = pstmt.executeUpdate();
			if(addedRows>0)
				return true;
			else
				throw new UserException("Already followed!!!");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("Someting went wrong with DB", e);
		}
	}
	
	public boolean unfollow(int followerID, int followedID) throws UserException {
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(REMOVE_FROM_FOLLOWED_USERS);
			pstmt.setInt(1, followerID);
			pstmt.setInt(2, followedID);
			int removedRows = pstmt.executeUpdate();
			if(removedRows>0)
				return true;
			else
				throw new UserException("Not followed in the first place!!");
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("Something went wrong with DB", e);
		}
	}
	
	public List<Integer> getAllFollowedUsers(int id) throws UserException{
		try {
			PreparedStatement pstmt = db.getConnection().prepareStatement(GET_FOLLOWED_USERS);
			pstmt.setInt(1, id);
			ResultSet resultSet = pstmt.executeQuery();
			List<Integer> followedUsersIds = new LinkedList<>();
			while(resultSet.next()) {
				int followedUserId = resultSet.getInt(2);
				followedUsersIds.add(followedUserId);
			}
			return followedUsersIds;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("DB DOWN", e);
		}
	}	
	public boolean checkIfFollowExistsInDb(int followerId,int followedId) throws UserException {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(CHECK_IF_FOLLOW_ALREADY_EXISTS);
				pstmt.setInt(1, followerId);
				pstmt.setInt(2, followedId);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next())
					return true;
				else
					return false;
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException("Something went wrong with DB", e);
			}

	
	}

	public boolean checkIfUserExistsInDB(Integer id) throws UserException {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(CHECK_IF_USER_EXISTS);
				pstmt.setInt(1, id);
				ResultSet resultSet = pstmt.executeQuery();
				if (resultSet.next()) 
					return true;
				 else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException("Something went wrong with DB",e);
			}
	}
}
