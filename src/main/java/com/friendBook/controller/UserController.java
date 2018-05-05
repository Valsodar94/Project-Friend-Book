package com.friendBook.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.friendBook.model.EmailSender;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;
import com.mysql.fabric.Response;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

@Controller
public class UserController {
	private static final String INVALID_CONFIRMATION_CODE = "The code is invalid. Please try again";
	private static final String EXPIRED_SESSION_ERROR_MESSAGE = "Your session has expired";
	private static final String ERROR_MESSAGE_FOR_TAKEN_EMAIL = "Email is already used, please enter a different email";
	private static final String ERROR_MESSAGE_FOR_USERNAME_TAKEN = "Username is taken, please enter a different username";
	private static final String ERROR_MESSAGE_FOR_PASSWORD_MISSMATCH = "passwords missmatch";

	@Autowired
	private UserDao uDao;
	@Autowired
	private EmailSender eSender;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpSession session, Model model, HttpServletResponse response){
		try {
			if(session.getAttribute("USER") == null) {
				try {
					int userId = uDao.login(username, password);
					if(uDao.checkIfAccountVerified(username)) {
						session.setAttribute("USER", username);
						session.setAttribute("USERID", userId);
						session.setMaxInactiveInterval(120);
						return "redirect:/";
					}
					else {
						model.addAttribute("username", username);
						return "AccountConfirmation";
					}
		
				}
				catch (LoginException e) {
					e.printStackTrace();
					model.addAttribute("error", "Username or password missmatch");
					return "index";
				}
			} else {
				return "redirect:/";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
		
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:/";
	}
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session, Model model) {
		try {
			if (session.getAttribute("USER") != null) {
				session.setAttribute("USER", null);
				session.setAttribute("USERID", null);
				session.invalidate();
				return"redirect:/";
			}
			return "redirect:/";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(Model model, HttpSession session) {
		try {
			if(session.getAttribute("USER")== null)
				return "RegistrationForm";
			else {
				return "redirect:/";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ServletException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String password2 = request.getParameter("password2");
			String email = request.getParameter("email");
	
			
	        if(!(password.equals(password2))){
	        	model.addAttribute("error", ERROR_MESSAGE_FOR_PASSWORD_MISSMATCH);
	        	return"RegistrationForm";            
	        }
			try {
				if(uDao.checkIfUsernameExistsInDB(username)) {
		            model.addAttribute("error", ERROR_MESSAGE_FOR_USERNAME_TAKEN);
		            return"RegistrationForm";
		            
				}
				if(uDao.checkIfEmailExistsInDB(email)) {
					model.addAttribute("error", ERROR_MESSAGE_FOR_TAKEN_EMAIL);
				    return"RegistrationForm";
				}
			    Random rand = new Random();
				Integer confirmationCode =rand.nextInt(10000) + 10000;
				User u = new User(0,username,password,email);
				u.setConfirmationCode(confirmationCode);
				uDao.register(u);
				
				eSender.sendEmail(email, "Friend-Book email confirmation", confirmationCode.toString());
				return "index";
			}
			catch(RegisterException | UserException e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", e.getMessage());
				return "ErrorPage";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
	
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public String follow(@RequestParam("profileID") String profileID, HttpSession session, Model model) {
		try {
			if(session.getAttribute("USERID") !=null && profileID!=null) {
				int followerId = (int) session.getAttribute("USERID");
				int followedId = Integer.parseInt(profileID);
				try {
					if(uDao.checkIfFollowExistsInDb(followerId, followedId)) {
						uDao.unfollow(followerId, followedId);
						session.setAttribute("followedUser", followedId);
						session.setAttribute("followMessage", "Unfollowed");
						return "redirect:/"+profileID;
					}
					else {
						uDao.follow(followerId, followedId);
						session.setAttribute("followMessage", "Followed");
						return "redirect:/"+profileID;
					}
				} catch (UserException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "ErrorPage";
				}
			}
			session.setAttribute("error", EXPIRED_SESSION_ERROR_MESSAGE);
			return "redirect:/";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}

	@RequestMapping(value = "/confirmAccount", method = RequestMethod.POST)
	public String confirmAccount(Model model, @RequestParam("code") String confirmationsCode, @RequestParam("username") String username, HttpSession session) {
		try {
			int confirmationCode = Integer.parseInt(confirmationsCode);
			if(uDao.checkConfirmationCode(confirmationCode, username)) {
				if(uDao.verifyAccount(username)) {
					session.setAttribute("USER", username);
					return "redirect:/";
				}
				session.setAttribute("error", "Something went wrong, please try again");
				return "redirect:/";
			}
			model.addAttribute("username", username);
			model.addAttribute("confirmationError", INVALID_CONFIRMATION_CODE);
			return "AccountConfirmation";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
}
