package com.friendBook.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.UserException;

@Controller
@RequestMapping(value="/{id}")
public class ProfileController {
	private static final String LOGIN_REQUIRED_ERROR = "You need to be logged in to see this menu.";
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";

	@Autowired
	private UserDao uDao;
	
	@RequestMapping(method =RequestMethod.GET, value = "/followers")
	public ModelAndView showFollowers(@PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			modelAndView.setViewName("Profile");
			if(session.getAttribute("USER")!=null) {
				try {
					List<User> followers = new LinkedList<>(uDao.getAllFollowers(id));
					modelAndView.addObject("users", followers);
					return modelAndView;
				} catch (UserException e) {
					e.printStackTrace();
					return new ModelAndView("ErrorPage" ,"errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
				}
			}
			modelAndView.addObject("error", LOGIN_REQUIRED_ERROR);
			return modelAndView;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage" ,"errorMessage", e.getMessage());
		}
	}
	
	@RequestMapping(method =RequestMethod.GET, value = "/followed")
	public ModelAndView showFollowed(@PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			modelAndView.setViewName("Profile");
			if(session.getAttribute("USER")!=null) {
				try {
					List<User> followed = new LinkedList<>(uDao.getAllFollowedUsers(id));
					modelAndView.addObject("users", followed);
					return modelAndView;
				} catch (UserException e) {
					e.printStackTrace();
					return new ModelAndView("ErrorPage" ,"errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
				}
			}
			modelAndView.addObject("error", LOGIN_REQUIRED_ERROR);
			return modelAndView;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage" ,"errorMessage", e.getMessage());
		}
	}
	
}
