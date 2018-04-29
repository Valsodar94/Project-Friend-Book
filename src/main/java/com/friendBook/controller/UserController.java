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
		try {
			int userId = uDao.login(username, password);
			session.setAttribute("USER", username);
			session.setAttribute("USERID", userId);
			
			session.setMaxInactiveInterval(120);
			return "index.jsp";

		}
		catch (LoginException e) {
			model.addAttribute("error", "Invalid username or password");
		    return"index.jsp";
		}
		
	}

	
	@RequestMapping(value = "logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session, Model model) {
		if (session.getAttribute("USER") != null) {
			session.setAttribute("USER", null);
			session.invalidate();
		}
		return "redirect:index.jsp";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, ServletException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");

		
        if(!(password.equals(password2))){
        	model.addAttribute("error", "passwords missmatch");
        	return"RegistrationForm.jsp";            
        }
		try {
			if(uDao.checkIfUsernameExistsInDB(username)) {
	            model.addAttribute("error", "Username is taken, please enter a different username");
	            return"RegistrationForm.jsp";
	            
			}
			if(uDao.checkIfEmailExistsInDB(email)) {
				model.addAttribute("error", "Email is already used, please enter a different email");
			    return"RegistrationForm.jsp";			}
			User u = new User(0,username,password,email);
			uDao.register(u);
			return "index.jsp";
		}
		catch(RegisterException | UserException | SQLException e) {
			e.printStackTrace();
			return"RegistrationForm.jsp";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		if (session.getAttribute("USER") != null) {
			session.setAttribute("USER", null);
			session.invalidate();
		}
		return "index.jsp";
	}
	
}
