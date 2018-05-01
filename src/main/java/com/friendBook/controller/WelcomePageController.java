package com.friendBook.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.PostException;
import exceptions.UserException;

@Controller
@RequestMapping(value="/index")
public class WelcomePageController {
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private UserDao uDao;
	
		@RequestMapping(method = RequestMethod.GET)
		public String sayHello(Model model, HttpServletRequest request) throws PostException {
			HttpSession session = request.getSession();
			if(session.getAttribute("USERID")!=null) {
				int userId = (int) session.getAttribute("USERID");
				try {
					List<User> followedUsers = new LinkedList<>(uDao.getAllFollowedUsers(userId));
					Set<Post> feed = new TreeSet<>();
					for(User u : followedUsers) {
						feed.addAll(postDao.extractPosts(u.getId()));
					}
					model.addAttribute("posts", feed);
					return "test";
				} catch (UserException e) {
					return "test";
				}
			}
			return "test";
		}	
}
