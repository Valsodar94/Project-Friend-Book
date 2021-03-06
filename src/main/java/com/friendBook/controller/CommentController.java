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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.Comment;
import com.friendBook.model.CommentAnswer;
import com.friendBook.model.CommentDAO;
import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;

import exceptions.CommentException;
import exceptions.LikeException;
import exceptions.PostException;
import exceptions.UserException;

@Controller
public class CommentController {
//	constants
	private static final String ERROR_MESSAGE_FOR_EMPTY_COMMENT = "You can't publish an empty content. Write a text.";
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";

	@Autowired
	private PostDao postDao;
	
	@Autowired
	private CommentDAO commentDao;
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
	public String getComments(@PathVariable("id") int postId, Model model, HttpServletRequest request, HttpSession session) {
		try {
			try {
				List<Comment> commentsOnPost = new LinkedList<>(commentDao.extractComments(postId));
				Collections.sort(commentsOnPost);
				model.addAttribute("comments", commentsOnPost);
				Post post = postDao.getPostById(postId);
				model.addAttribute("post", post);
				return "CommentList";
			} catch (CommentException | PostException e) {
				e.printStackTrace();
				session.setAttribute("errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
				return "redirect:/error";

			}
		}
		catch(Exception e){
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value = "/comment/{id}", method = RequestMethod.POST)
	public String putComment(@PathVariable("id") int postId, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String commentText = request.getParameter("commentText");
		try {			
			if (!(commentText == null || commentText.length() == 0)) {	
				User user = (User) session.getAttribute("user");
				int userId = user.getId();
				Comment newComment = new Comment(0, userId, postId);
				newComment.setText(commentText);
				commentDao.putComment(newComment);	
				model.addAttribute("comment", newComment);
				return "redirect:../comment/"+postId;
			}
			model.addAttribute("errorMessage", ERROR_MESSAGE_FOR_EMPTY_COMMENT);
			return "error";
		} catch (CommentException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value = "/comment/{postId}/answer/{commentId}", method = RequestMethod.GET)
	public ModelAndView getAnswers(@PathVariable("postId") int postId, 
			@PathVariable("commentId") int commentId,
			ModelAndView modelAndView, HttpServletRequest request) {
		try {
			try {
				List<CommentAnswer> answersOnComment = new LinkedList<>(commentDao.extractAnswers(commentId));
				Collections.sort(answersOnComment);
				modelAndView.addObject("answers", answersOnComment);
				modelAndView.setViewName("CommentList");
				return modelAndView;
			} catch (CommentException e) {
				e.printStackTrace();
				return new ModelAndView("error","errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return new ModelAndView("error","errorMessage", e.getMessage());
		}
	}
	
	@RequestMapping(value = "/comment/{postId}/answer/{commentId}", method = RequestMethod.POST)
	public String putAnswer(@PathVariable("commentId") int commentId,
			@PathVariable("postId") int postId,
			Model model, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			String answerText = request.getParameter("answerText");
			try {			
				if (!(answerText == null || answerText.length() == 0)) {	
					User user = (User) session.getAttribute("user");
					int userId = user.getId();
					CommentAnswer newAnswer = new CommentAnswer(0, userId, postId, commentId);
					newAnswer.setText(answerText);
					commentDao.answerComment(newAnswer);	
					return "redirect:/comment/" + postId;
				}
				model.addAttribute("errorMessage", ERROR_MESSAGE_FOR_EMPTY_COMMENT);
				return "error";
			} catch (CommentException e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", e.getMessage());
				return "error";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value = "/comment/{postId}/delete", method = RequestMethod.POST)
	public String deleteAnswer(@PathVariable("postId") int postId, @RequestParam("answerId")int answerId, @RequestParam("answerAuthorId")int answerAuthorId, Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")!=null) {
				User user = (User) session.getAttribute("user");
				int sessionUserId = user.getId();
				if(sessionUserId!= answerAuthorId && user.isAdmin()==false) {
					return "redirect:/";
				}
				else {
					if(commentDao.deleteAnwer(answerId)) {
						return "redirect:/comment/"+postId;
					}
					else {
						session.setAttribute("CommentMessage", "As you can see something went wrong and the answer is still there");
						return "redirect:/comment/"+postId;
					}
				}
			}
			else {
				return "redirect:/";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value = "/comment/{postId}/deleteComment", method = RequestMethod.POST)
	public String deleteComment(@PathVariable("postId") int postId, @RequestParam("commentId")int commentId, @RequestParam("commentAuthorId")int commentAuthorId, Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")!=null) {
				User user = (User) session.getAttribute("user");
				int sessionUserId = user.getId();
				if(sessionUserId!= commentAuthorId && user.isAdmin()==false) {
					return "redirect:/";
				}
				else {
					if(commentDao.deleteComment(commentId)) {
						return "redirect:/comment/"+postId;
					}
					else {
						session.setAttribute("CommentMessage", "As you can see something went wrong and the comment is still there");
						return "redirect:/comment/"+postId;
					}
				}
			}
			else {
				return "redirect:/";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
}
