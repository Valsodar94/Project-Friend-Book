package com.friendBook.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.UserException;

@Controller
public class SearchController {
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";


	@Autowired
	private UserDao uDao;
	
	@RequestMapping(value = {"/SearchResult", "/profile/SearchResult"}, method = RequestMethod.GET)
	public ModelAndView showSearchResults(@RequestParam("search") String search) {
		try {
			List<User> users = new LinkedList<>();
			try {
				users.addAll(uDao.getUsersByString(search));
				return new ModelAndView("SearchResult", "users", users);
	
			} catch (UserException e) {
				return new ModelAndView("ErrorPage", "errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage","errorMessage", e.getMessage());
		}
	}
	
}
