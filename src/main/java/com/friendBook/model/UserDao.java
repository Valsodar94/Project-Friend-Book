package com.friendBook.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import exceptions.LikeException;
import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;
@Component
public class UserDao implements IUserDao{
//	constants
	private static final String NO_SUCH_USER_ERROR = "No such User";
	private static final String ERROR_MESSAGE_FOR_FOLLOW = "Already followed!!!";
	private static final String ERROR_MESSAGE_FOR_UNFOLLOW = "Not followed in the first place!!";
	private static final String ERROR_MESSAGE_FOR_INVALID_ID = "Invalid id";
	private static final String ERROR_MESSAGE_FOR_NULL = "username or password is null!!!";
	private static final String DB_ERROR_MESSAGE = "Something went wrong with the database!";
	private static final String FAIL_LOGIN_MESSAGE = "No such user!";
//DB statements	
	private static final String INSERT_INTO_FOLLOWED_USERS = "INSERT INTO followed_users VALUES (?, ?)";
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE user_name=? and user_pass = sha1(?)";
	private static final String ADD_USER_SQL = "INSERT INTO users VALUES (null, ?, sha1(?), ?, ?, 0)";
	private static final String CHECK_FOR_USERNAME = "SELECT * FROM users WHERE user_name=?";
	private static final String CHECK_FOR_EMAIL = "SELECT * FROM users WHERE user_email=?";
	private static final String SELECT_USER = "SELECT * FROM users WHERE user_id=?;";
	private static final String GET_USERS_BY_STRING = "SELECT * FROM users WHERE user_name like ?";
	private static final String REMOVE_FROM_FOLLOWED_USERS = "DELETE FROM followed_users WHERE user_id = ? AND followed_user_id = ?";
	private static final String GET_FOLLOWED_USERS = "select u.user_id, u.user_name, u.user_pass, u.user_email\n" + 
			"from users u join followed_users f\n" + 
			"on(u.user_id = f.followed_user_id)\n" + 
			"where f.user_id = ?";
	private static final String CHECK_IF_FOLLOW_ALREADY_EXISTS = "SELECT * FROM followed_users WHERE user_id = ? AND followed_user_id =?";
	private static final String CHECK_IF_USER_EXISTS = "SELECT * FROM users WHERE user_id=?";
	private static final String EXTRACT_ALL_FOLLOWERS = "select *\n" + 
			"from followed_users f join users u\n" + 
			"using(user_id)\n" + 
			"where followed_user_id = ?";
	private static final String VERIFY_ACCOUNT = "UPDATE users\r\n" + 
			"SET is_confirmed = 1\r\n" + 
			"WHERE user_name =?";
	private static final String RESET_PASS = "UPDATE users\n" + 
			"SET user_pass = sha1(?)\n" + 
			"WHERE user_email = ?";
	private static final String EDIT_PROFILE = "UPDATE users \r\n" + 
			"SET user_email = ?, user_pass = sha1(?)\r\n" + 
			"WHERE user_id = ?";
	
	@Autowired
	private DBConnection db;

	
	
	@Override
	public int login(String username, String password) throws LoginException {
		PreparedStatement pstmt;
		if(username!=null && password!=null) {
			try {
				pstmt = db.getConnection().prepareStatement(LOGIN_USER_SQL);
				pstmt.setString(1, username);
				pstmt.setString(2, password);
				
				ResultSet resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				throw new LoginException(FAIL_LOGIN_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LoginException(DB_ERROR_MESSAGE, e);
			} 
		}
		else
			throw new LoginException(ERROR_MESSAGE_FOR_NULL);

	}
	
	@Override
	public int register(User u) throws RegisterException {
		PreparedStatement pstmt;
		if(u!=null) {
			try {
				pstmt = db.getConnection().prepareStatement(ADD_USER_SQL, Statement.RETURN_GENERATED_KEYS);			
				
				pstmt.setString(1, u.getUsername());
				pstmt.setString(2, u.getPassword());			
				pstmt.setString(3, u.getEmail());	
				pstmt.setInt(4, u.getConfirmationCode());
				pstmt.executeUpdate();
				
				ResultSet resultSet = pstmt.getGeneratedKeys();
				resultSet.next();		
				return resultSet.getInt(1);
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RegisterException(DB_ERROR_MESSAGE, e);
			} 
		}
		else
			throw new RegisterException("User is null!!!");
	}
	
	public boolean checkIfUsernameExistsInDB(String username)throws UserException {
		if(username!=null) {
			PreparedStatement pstmt;
			try {
				pstmt = db.getConnection()
						.prepareStatement(CHECK_FOR_USERNAME);
				pstmt.setString(1, username);
				ResultSet resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) 
					return true;
				 else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE);
			}

		}
		else
			throw new UserException("username is null");
	}
	
	public boolean checkIfEmailExistsInDB(String email) throws UserException {
		if(email!=null) {
				try {
					PreparedStatement pstmt = db.getConnection()
							.prepareStatement(CHECK_FOR_EMAIL);
						pstmt.setString(1, email);
						ResultSet resultSet = pstmt.executeQuery();
					if (resultSet.next()) 
						return true;
					 else
						return false;
				} catch (SQLException e) {
					e.printStackTrace();
					throw new UserException(DB_ERROR_MESSAGE);

				}
		}
		else
			throw new UserException("email is null");
	}
	public User getUserById(int id) throws UserException {
		if(id>0) {
			PreparedStatement pstmt;
			try {
				pstmt = db.getConnection().prepareStatement(SELECT_USER);
				pstmt.setInt(1, id);
				
				ResultSet resultSet = pstmt.executeQuery();
				if(resultSet.next()) {
					String username = resultSet.getString(2); 
					String password = resultSet.getString(3); 
					String email = resultSet.getString(4); 
					return new User(id,username,password,email);
				}
				else {
					throw new UserException(NO_SUCH_USER_ERROR);

				}
			}
			catch(SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);

	}
	public List<User> getUsersByString(String name) throws UserException{
		if(name!=null) {
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
				throw new UserException(DB_ERROR_MESSAGE);
			}
		}
		else
			throw new UserException("name is null");

	}
	public boolean follow(int followerID, int followedID) throws UserException {
		if(followerID > 0 && followedID > 0) {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(INSERT_INTO_FOLLOWED_USERS);
				pstmt.setInt(1, followerID);
				pstmt.setInt(2, followedID);
				int addedRows = pstmt.executeUpdate();
				if(addedRows>0)
					return true;
				else
					throw new UserException(ERROR_MESSAGE_FOR_FOLLOW);
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}	
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);

	}
	
	public boolean unfollow(int followerID, int followedID) throws UserException {
		if(followerID > 0 && followedID > 0) {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(REMOVE_FROM_FOLLOWED_USERS);
				pstmt.setInt(1, followerID);
				pstmt.setInt(2, followedID);
				int removedRows = pstmt.executeUpdate();
				if(removedRows>0)
					return true;
				else
					throw new UserException(ERROR_MESSAGE_FOR_UNFOLLOW);
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);
	}
	
	public List<User> getAllFollowedUsers(int id) throws UserException{
		if(id > 0) {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(GET_FOLLOWED_USERS);
				pstmt.setInt(1, id);
				ResultSet resultSet = pstmt.executeQuery();
				List<User> followedUsers = new LinkedList<>();
				while(resultSet.next()) {
					int id2 = resultSet.getInt(1);
					String username = resultSet.getString(2);
					String password = resultSet.getString(3);
					String email = resultSet.getString(4);
					User u = new User(id2,username,password, email);
					followedUsers.add(u);
				}
				return followedUsers;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);
	}	
	public List<User> getAllFollowers(int id) throws UserException{
		if(id > 0) {
			try {
				PreparedStatement pstmt = db.getConnection().prepareStatement(EXTRACT_ALL_FOLLOWERS);
				pstmt.setInt(1, id);
				ResultSet resultSet = pstmt.executeQuery();
				List<User> followers = new LinkedList<>();
				while(resultSet.next()) {
					int id2 = resultSet.getInt(1);
					String username = resultSet.getString(3);
					String password = resultSet.getString(4);
					String email = resultSet.getString(5);
					User u = new User(id2,username,password, email);
					followers.add(u);
				}
				return followers;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);

	}
	
	public boolean checkIfFollowExistsInDb(int followerId,int followedId) throws UserException {
		if(followerId > 0 && followedId > 0) {
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
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);

	}

	public boolean checkIfUserExistsInDB(Integer id) throws UserException {
		if(id > 0) {
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
				throw new UserException(DB_ERROR_MESSAGE,e);
			}
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_INVALID_ID);
	}

	public boolean checkIfAccountVerified(String username) throws UserException {
		PreparedStatement pstmt;
		if(username!=null) {
			try {
				pstmt = db.getConnection().prepareStatement(CHECK_FOR_USERNAME);
				pstmt.setString(1, username);				
				ResultSet resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) {
					return resultSet.getBoolean(6);
				}
				throw new UserException(FAIL_LOGIN_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			} 
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_NULL);
	}

	public boolean checkConfirmationCode(int confirmationCode, String username) throws UserException {
		PreparedStatement pstmt;
		if(username!=null) {
			try {
				pstmt = db.getConnection().prepareStatement(CHECK_FOR_USERNAME);
				pstmt.setString(1, username);				
				ResultSet resultSet = pstmt.executeQuery();
				
				if (resultSet.next()) {
					int accountVerificationCode = resultSet.getInt(5);
					if(confirmationCode == accountVerificationCode) {
						return true;
					}
					return false;
				}
				throw new UserException(FAIL_LOGIN_MESSAGE);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			} 
		}
		else
			throw new UserException(ERROR_MESSAGE_FOR_NULL);
	}

	public boolean verifyAccount(String username) throws UserException {
		if(username!=null) {
			PreparedStatement pstmt;
			try {
				pstmt = db.getConnection().prepareStatement(VERIFY_ACCOUNT);
				pstmt.setString(1, username);
				int rowsUpdated = pstmt.executeUpdate();
				if(rowsUpdated>0)
					return true;
				else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		throw new UserException(ERROR_MESSAGE_FOR_NULL);
	}

	public boolean resetPassword(String email, String newPassword) throws UserException {
		if(email!=null) {
			PreparedStatement pstmt;
			try {
				pstmt = db.getConnection().prepareStatement(RESET_PASS);
				pstmt.setString(1, newPassword);
				pstmt.setString(2, email);
				int rowsUpdated = pstmt.executeUpdate();
				if(rowsUpdated>0)
					return true;
				else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
		}
		throw new UserException(ERROR_MESSAGE_FOR_NULL);
	}

	public boolean editProfile(int id, String pass, String email) throws UserException {
		if(id>0 && pass !=null && email!=null) {
			PreparedStatement pstmt;
			try {
				pstmt = db.getConnection().prepareStatement(EDIT_PROFILE);
				pstmt.setString(1, email);
				pstmt.setString(2, pass);
				pstmt.setInt(3, id);
				int rowsUpdated = pstmt.executeUpdate();
				if(rowsUpdated>0)
					return true;
				else
					return false;
			}
			catch(SQLException e) {
				e.printStackTrace();
				throw new UserException(DB_ERROR_MESSAGE, e);
			}
			
		}
		throw new UserException(ERROR_MESSAGE_FOR_NULL);

	}
}
