package com.friendBook.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

@Controller
public class UserController {
	@Autowired
	private UserDao uDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Model model, HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		try {
			int userId = uDao.login(username, password);
			session.setAttribute("USER", username);
			session.setAttribute("USERID", userId);
			session.setAttribute("nmb", uDao.getUsersByString("r").size());
			session.setMaxInactiveInterval(120);
		
		}
		catch (LoginException | UserException e) {
			return "redirect:InvalidLogin.html";
		}
		return"redirect:index";
	}
	
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session, Model model) {
		if (session.getAttribute("USER") != null) {
			session.setAttribute("USER", null);
			session.invalidate();
		}
		return "index";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");

		
        if(!(password.equals(password2))){
            request.setAttribute("error", "passwords missmatch");
            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
            
        }
		try {
			if(uDao.checkIfUsernameExistsInDB(username)) {
	            request.setAttribute("error", "Username is taken, please enter a different username");
	            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
	            
			}
			if(uDao.checkIfEmailExistsInDB(email)) {
			    request.setAttribute("error", "Email is already used, please enter a different email");
	            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
			}
			User u = new User(0,username,password,email);
			int userId = uDao.register(u);
			session.setAttribute("USER", username);
			session.setAttribute("USERID", userId);
			session.setMaxInactiveInterval(120);
			response.sendRedirect("MainServlet");
		}
		catch(RegisterException | UserException | SQLException e) {
			e.printStackTrace();
			response.sendRedirect("RegistrationForm.jsp");
		}
		return "index";
	}
}
