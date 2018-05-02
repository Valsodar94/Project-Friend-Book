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
import com.friendBook.model.CommentAnswer;
import com.friendBook.model.CommentDAO;
import com.friendBook.model.Post;
import com.friendBook.model.PostDao;

import exceptions.CommentException;
import exceptions.LikeException;
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
				return "CommentList";
			} catch (CommentException | LikeException | PostException e) {
				e.printStackTrace();
				return "redirect:test";
			}
		} 
		return "CommentList";
	}
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.POST)
	public String putComment(@PathVariable("id") int postId, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String commentText = request.getParameter("commentText");
		try {			
			if (!(commentText == null || commentText.length() == 0)) {	
				int userId = (int) session.getAttribute("USERID");
				Comment newComment = new Comment(0, userId, postId);
				newComment.setText(commentText);
				commentDao.putComment(newComment);	
				model.addAttribute("comment", newComment);
				return "redirect:../comment/"+postId;
			}
			return "redirect:/";
		} catch (CommentException e) {
			e.printStackTrace();
			return "redirect:test";
		}
	}
	
	@RequestMapping(value = "/comment/{postId}/answer/{commentId}", method = RequestMethod.GET)
	public ModelAndView getAnswers(@PathVariable("postId") int postId, 
			@PathVariable("commentId") int commentId,
			ModelAndView modelAndView, HttpServletRequest request) {
//		if(postId >= 0) {
			try {
				List<CommentAnswer> answersOnComment = new LinkedList<>(commentDao.extractAnswers(commentId));
				Collections.sort(answersOnComment);
				modelAndView.addObject("answers", answersOnComment);
				modelAndView.setViewName("CommentList");
//				Post post = postDao.getPostById(postId);
//				model.addAttribute("post", post);
//				Comment comment = commentDao.getCommentById(commentId);
//				model.addAttribute("comment", comment);
				return modelAndView;
			} catch (CommentException e) {
				e.printStackTrace();
				return new ModelAndView("test", "error", e.getMessage());
			}
//		} 
//		return "CommentList";
	}
	
	@RequestMapping(value = "/comment/{postId}/answer/{commentId}", method = RequestMethod.POST)
	public String putAnswer(@PathVariable("commentId") int commentId,
			@PathVariable("postId") int postId,
			Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String answerText = request.getParameter("answerText");
		try {			
			if (!(answerText == null || answerText.length() == 0)) {	
				int userId = (int) session.getAttribute("USERID");
				CommentAnswer newAnswer = new CommentAnswer(0, userId, postId, commentId);
				newAnswer.setText(answerText);
				commentDao.answerComment(newAnswer);	
				model.addAttribute("answer", newAnswer);
				return "redirect:../comment/" + postId + "/answer/" + commentId;
			}
			return "redirect:/";
		} catch (CommentException e) {
			e.printStackTrace();
			return "redirect:test";
		}
	}

}
