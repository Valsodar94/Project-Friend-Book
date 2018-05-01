package com.friendBook.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.io.IOException;
import java.sql.SQLException;

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

import com.friendBook.model.User;
import com.friendBook.model.UserDao;
import com.mysql.fabric.Response;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

@Controller
public class UserController {
	@Autowired
	private UserDao uDao;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpSession session, Model model, HttpServletResponse response) throws IOException {
		if(session.getAttribute("USER") == null) {
			try {
				int userId = uDao.login(username, password);
				session.setAttribute("USER", username);
				session.setAttribute("USERID", userId);
				
				session.setMaxInactiveInterval(120);
				return "redirect:/";
	
			}
			catch (LoginException e) {
				model.addAttribute("error", "Invalid username or password");
			    return"test";
			}
		} else {
			model.addAttribute("error", "You are already logged");
			return "test";
		}
		
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:/";
	}
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session, Model model) {
		if (session.getAttribute("USER") != null) {
			session.setAttribute("USER", null);
			session.setAttribute("USERID", null);
			session.invalidate();
		}
		return "redirect:/";
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(HttpSession session) {
		if(session.getAttribute("USER")== null)
			return "RegistrationForm";
		else
			return "redirect:/";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ServletException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");

		
        if(!(password.equals(password2))){
        	model.addAttribute("error", "passwords missmatch");
        	return"RegistrationForm";            
        }
		try {
			if(uDao.checkIfUsernameExistsInDB(username)) {
	            model.addAttribute("error", "Username is taken, please enter a different username");
	            return"RegistrationForm";
	            
			}
			if(uDao.checkIfEmailExistsInDB(email)) {
				model.addAttribute("error", "Email is already used, please enter a different email");
			    return"RegistrationForm";			}
			User u = new User(0,username,password,email);
			uDao.register(u);
			return "test";
		}
		catch(RegisterException | UserException | SQLException e) {
			e.printStackTrace();
			return"RegistrationForm";
		}
	}
	
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public String follow(@RequestParam("profileID") String profileID, HttpSession session, Model model) {
		
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
				session.setAttribute("error", "Something went wrong");
				return "redirect:/";
			}
		}
		session.setAttribute("error", "Your session has expired");
		return "redirect:/";
	}


}
