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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;
import com.friendBook.model.EmailSender;
import com.friendBook.model.RandomCodeGenerator;
import com.mysql.fabric.Response;

import exceptions.LoginException;
import exceptions.RegisterException;
import exceptions.UserException;

@Component
@Controller
public class UserController implements RandomCodeGenerator {
	private static final String PASSWORD_RESET_TEXT = "You have successfully reset your password. Your new password is: ";
	private static final String TITLE_FOR_PASSWORD_RESET_EMAIL = "Friend-Book password reset";
	private static final String EMAIL_REGISTRATION_TITLE = "Friend-Book email confirmation";
	private static final String UNEXPECTED_ERROR = "Something went wrong, please try again";
	private static final int SESSION_TIMEOUT = 60*10;
	private static final String INVALID_EMAIL_ERROR = "No user with such mail has been registered";
	private static final String INVALID_CONFIRMATION_CODE = "The code is invalid. Please try again";
	private static final String EXPIRED_SESSION_ERROR_MESSAGE = "Your session has expired";
	private static final String ERROR_MESSAGE_FOR_TAKEN_EMAIL = "Email is already used, please enter a different email";
	private static final String ERROR_MESSAGE_FOR_USERNAME_TAKEN = "Username is taken, please enter a different username";
	private static final String ERROR_MESSAGE_FOR_PASSWORD_MISSMATCH = "passwords missmatch";
	private static final String EMAIL_REGISTRATION_MESSAGE = "Congratulations you have successfully registered in Friend Book. This is your code to complete your registration: ";

	@Autowired
	private UserDao uDao;
	@Autowired
	private EmailSender eSender;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpSession session, Model model, HttpServletResponse response){
		try {
			if(session.getAttribute("user") == null) {
				try {
					User user = uDao.login(username, password);
					if(uDao.checkIfAccountVerified(username)) {
						session.setAttribute("user", user);
						session.setMaxInactiveInterval(SESSION_TIMEOUT);
						
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
			return "error";
		}
		
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "redirect:/";
	}
	
	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public String logOut(HttpSession session, Model model) {
		try {
			if (session.getAttribute("user") != null) {
				session.setAttribute("user", null);
				session.invalidate();
				return"redirect:/";
			}
			return "redirect:/";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegistrationForm(Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")== null)
				return "RegistrationForm";
			else {
				return "redirect:/";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
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
	        	model.addAttribute("registrationError", ERROR_MESSAGE_FOR_PASSWORD_MISSMATCH);
	        	return"RegistrationForm";            
	        }
			try {
				if(uDao.checkIfUsernameExistsInDB(username)) {
		            model.addAttribute("registrationError", ERROR_MESSAGE_FOR_USERNAME_TAKEN);
		            return"RegistrationForm";
		            
				}
				if(uDao.checkIfEmailExistsInDB(email)) {
					model.addAttribute("registrationError", ERROR_MESSAGE_FOR_TAKEN_EMAIL);
				    return"RegistrationForm";
				}
				Integer confirmationCode =RandomCodeGenerator.generateRandomCode();
				User u = new User(0,username,password,email);
				u.setConfirmationCode(confirmationCode);
				uDao.register(u);
				
				eSender.sendEmail(email, EMAIL_REGISTRATION_TITLE, EMAIL_REGISTRATION_MESSAGE+confirmationCode.toString());
				return "redirect:/";
			}
			catch(RegisterException | UserException e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", e.getMessage());
				return "error";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}

	

	@RequestMapping(value = "/confirmAccount", method = RequestMethod.POST)
	public String confirmAccount(Model model, @RequestParam("code") String confirmationsCode, @RequestParam("username") String username, HttpSession session) {
		try {
				int confirmationCode = Integer.parseInt(confirmationsCode);
				if(uDao.checkConfirmationCode(confirmationCode, username)) {
					if(uDao.verifyAccount(username)) {
						session.setAttribute("accountConfirmMessage", "You have confirmed your account sucessfully.");
						return "redirect:/";
					}
					session.setAttribute("error", UNEXPECTED_ERROR);
					return "redirect:/";
				}
				model.addAttribute("username", username);
				model.addAttribute("confirmationError", INVALID_CONFIRMATION_CODE);
				return "AccountConfirmation";
			
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value = "/forgottenPass", method = RequestMethod.GET)
	public String showForgottenPassView(HttpSession session,Model model) {
		try {
			if(session.getAttribute("user")!=null) {
				return "redirect:/";
			}
			else {
				return "ForgottenPassword";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPass(Model model, @RequestParam("email") String email,HttpSession session) {
		try {
			if(uDao.checkIfEmailExistsInDB(email)) {
				String newPass = ""+RandomCodeGenerator.generateRandomCode();
				if(uDao.resetPassword(email, newPass)) {
					eSender.sendEmail(email, TITLE_FOR_PASSWORD_RESET_EMAIL, PASSWORD_RESET_TEXT+newPass);
					return "redirect:/";
				}
				session.setAttribute("error", UNEXPECTED_ERROR);
				return "redirect:/";
			}
			else {
				model.addAttribute("resetPassError", INVALID_EMAIL_ERROR);
				return"ForgottenPassword";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
}
