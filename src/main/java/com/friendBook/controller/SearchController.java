package com.friendBook.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.UserException;

@Controller
public class SearchController {

	@Autowired
	private UserDao uDao;
	
	@RequestMapping(value = "/SearchResult", method = RequestMethod.GET)
	public String showSearchResults(@RequestParam("search") String search, Model model) {
		List<User> users = new LinkedList<>();
		try {
			users = uDao.getUsersByString(search);
			model.addAttribute("users", users);
			return "SearchResult?search="+search;
		} catch (UserException e) {
			return "index.jsp";
		}
	}
	
}
