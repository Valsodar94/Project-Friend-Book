package com.friendBook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.LikeDao;

import exceptions.LikeException;
import exceptions.UserException;

@Controller
public class LikeController {
	
	@Autowired
	private LikeDao likeDao;
	
	
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	public String like(@RequestParam("postId") String postId, HttpSession session, Model model) {
		if(session.getAttribute("USERID") !=null) {
			int userId = (int) session.getAttribute("USERID");
			int postID = Integer.parseInt(postId);
			
			try {
				if(likeDao.checkIfLikeExistsInDb(postID, userId)) {
					likeDao.dislikeAPost(postID, userId);
					session.setAttribute("PostMessage", "The post has been disliked");
					session.setAttribute("postId", postId);
					return "redirect:/posts";
				}
				else {
					likeDao.likeAPost(postID, userId);
					session.setAttribute("PostMessage", "The post has been liked");
					session.setAttribute("postId", postId);
					return "redirect:/posts";
				}
			} catch (LikeException e) {
//				sent to a proper error page
				e.printStackTrace();
				return "redirect:/";
			}
		}
		model.addAttribute("error", "Your session has expired. You need to login");
		return "/";

	}
	
}
