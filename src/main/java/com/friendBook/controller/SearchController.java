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

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.UserException;

@Controller
public class SearchController {
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";


	@Autowired
	private UserDao uDao;
	@Autowired
	private PostDao pDao;
	
	@RequestMapping(value = {"/SearchResult", "/profile/SearchResult"}, method = RequestMethod.GET)
	public ModelAndView showSearchResults(@RequestParam("search") String search, ModelAndView modelAndView) {
		try {
			List<User> users = new LinkedList<>();
			List<Post> posts = new LinkedList<>();
			try {
				posts.addAll(pDao.extractPostsByTag(search));
				users.addAll(uDao.getUsersByString(search));
				modelAndView.addObject("users", users);
				modelAndView.addObject("posts", posts);
				modelAndView.setViewName("SearchResult");
				return modelAndView;
	
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
