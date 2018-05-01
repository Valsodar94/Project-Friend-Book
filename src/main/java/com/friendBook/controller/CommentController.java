package com.friendBook.controller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.Comment;
import com.friendBook.model.CommentDAO;
import com.friendBook.model.Post;
import com.friendBook.model.PostDao;

import exceptions.CommentException;
import exceptions.PostException;
import exceptions.UserException;

@Controller
public class CommentController {
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private CommentDAO commentDao;
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
	public String getComments(@PathVariable("id") int postId, Model model, HttpServletRequest request) {
		if(postId >= 0) {
			try {
				List<Comment> commentsOnPost = new LinkedList<>(commentDao.extractComments(postId));
				Collections.sort(commentsOnPost);
				model.addAttribute("comments", commentsOnPost);
				Post post = postDao.getPostById(postId);
				model.addAttribute("post", post);
			} catch (CommentException | PostException e) {
				e.printStackTrace();
				return "redirect:test";
			}
		} 
		return "CommentsView";
	}
	
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public String putComment(Model model, HttpServletRequest request) {
		String commentText = request.getParameter("commentText");
		try {			
			if (!(commentText == null || commentText.length() == 0)) {	
				HttpSession session = request.getSession();
				int userId = (int) session.getAttribute("USERID");
				int postId = (int) session.getAttribute("POSTID");
				Comment newComment = new Comment(0, userId, postId);
				newComment.setText(commentText);
				commentDao.putComment(newComment);	
				model.addAttribute("comment", newComment);
				return "redirect:/"+postId;
			}
			return "redirect:/";
		} catch (CommentException e) {
			e.printStackTrace();
			return "redirect:test";
		}
	}

}
