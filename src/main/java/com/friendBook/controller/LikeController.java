package com.friendBook.controller;

import java.net.URI;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.friendBook.model.LikeDao;

import exceptions.LikeException;
import exceptions.UserException;

@Controller
public class LikeController {
	
	private static final String LIKED_COMMENT_MESSAGE = "The comment has been liked";
	private static final String SESSION_EXPIRED_MESSAGE = "Your session has expired. You need to login";
	private static final Object DISLIKE_MESSAGE = "The post has been disliked";
	private static final Object LIKE_MESSAGE = "The post has been liked";
	private static final Object DISLIKED_COMMENT_MESSAGE = "The comment has been disliked";
	@Autowired
	private LikeDao likeDao;
	
	
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	public String like(@RequestParam("postId") String postId,@RequestParam(value = "profileId", required = false) String profileId, HttpSession session, Model model) {
		try {
			if(session.getAttribute("USERID") !=null) {
				int userId = (int) session.getAttribute("USERID");
				int postID = Integer.parseInt(postId);
				ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
				URI newUri = builder.build().toUri();
				String url = newUri.toString();
				url = url.replace("like", "");
				if(profileId != null)
					url += profileId;
				try {
					if(likeDao.checkIfLikeExistsInDb(postID, userId)) {
						likeDao.dislikeAPost(postID, userId);
						session.setAttribute("PostMessage", DISLIKE_MESSAGE);
						session.setAttribute("postId", postId);
						return "redirect:"+url;
					}
					else {
						likeDao.likeAPost(postID, userId);
						session.setAttribute("PostMessage", LIKE_MESSAGE);
						session.setAttribute("postId", postId);
						return "redirect:"+url;
					}
				} catch (LikeException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";
				}
			}
			model.addAttribute("error", SESSION_EXPIRED_MESSAGE);
			return "index";
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}

	}
	
	@RequestMapping(value = "/comment/like", method = RequestMethod.POST)
	public String likeComment(@RequestParam("postId") String postId,@RequestParam("commentId") String commentId, HttpSession session, Model model) {
		try {
			if(session.getAttribute("USERID") !=null) {
				int userId = (int) session.getAttribute("USERID");
				int commentID = Integer.parseInt(commentId);
				
				try {
					if(likeDao.checkIfLikeCommentExistsInDb(commentID, userId)) {
						likeDao.dislikeAComment(commentID, userId);
						session.setAttribute("CommentMessage", DISLIKED_COMMENT_MESSAGE);
						session.setAttribute("commentId", commentID);
						return "redirect:/comment/"+postId;
					}
					else {
						likeDao.likeAComment(commentID, userId);
						session.setAttribute("CommentMessage", LIKED_COMMENT_MESSAGE);
						session.setAttribute("commentId", commentID);
						return "redirect:/comment/"+postId;
					}
				} catch (LikeException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";				}
			}
			model.addAttribute("error", SESSION_EXPIRED_MESSAGE);
			return "index";
			
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	
}
