package com.friendBook.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.PostException;
import exceptions.UserException;

@Controller
public class PostController {
	private static final String ERROR_MESSAGE_FOR_EMPTY_POST = "You can't publish an empty post. Write a text or upload a picture.";

	// constants
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";
	private static final String SESSION_EXPIRED_MESSAGE = "Your session has expired. You need to login";
	@Autowired
	private PostDao postDao;

	@Autowired
	private UserDao uDao;
	
	@RequestMapping
	public String fallbackMethod(){
		return "redirect:/index";
	}

	@RequestMapping(value ="/{id:[\\d]+}", method = RequestMethod.GET)
	public ModelAndView showProfile(@PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			try {			
				if (uDao.checkIfUserExistsInDB(id)) {
					List<Post> postsList = new LinkedList<>(postDao.extractPosts(id));
					modelAndView.addObject("posts", postsList);
					modelAndView.addObject("id", id);
					modelAndView.setViewName("Profile");
					return modelAndView;
				} else
					return new ModelAndView("ErrorPage", "errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			} catch (UserException | PostException e) {
				e.printStackTrace();
				e.printStackTrace();
				return new ModelAndView("ErrorPage", "errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage", "errorMessage", e.getMessage());
		}

	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String publish(@RequestParam("tags") String tags, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("utf-8");
			if (session.getAttribute("USER") != null) {
				String text = request.getParameter("postText");
				System.out.println("[DEBUG] PostController .publish " + text);
				String pictureUrl = request.getParameter("pictureUrl");
				String picture = extractPictureName(pictureUrl);

				try {
					if (!((text == null || text.length() == 0) && (picture == null || picture.length() == 0))) {

						int userId = (int) session.getAttribute("USERID");
						Post newPost = new Post(0, userId);
						newPost.setText(text);
						newPost.setPictureUrl(picture);
						newPost.setTags(tags);
						postDao.publish(newPost);
						model.addAttribute("post", newPost);
						return "redirect:/" + userId;
					}
					model.addAttribute("errorMessage", ERROR_MESSAGE_FOR_EMPTY_POST);
					return "ErrorPage";
				} catch (PostException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "ErrorPage";
				}
			} else {
				session.setAttribute("error", SESSION_EXPIRED_MESSAGE);
				return "redirect:/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}

	// TODO da se napravqt pyrvo v PostDAO

	public void edit() throws PostException {

	}

	private String extractPictureName(String pictureUrl) {
		if (pictureUrl.length() > 0) {
			int symb = pictureUrl.length() - 1;
			for (; symb > 0; symb--) {
				if (pictureUrl.charAt(symb) == '/' || pictureUrl.charAt(symb) == '\\') {
					symb++;
					break;
				}
			}
			return pictureUrl.substring(symb);
		}
		return "";
	}
	
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public String deletePost(@RequestParam("postId")int postId, @RequestParam("postAuthorId")int postAuthorId, Model model, HttpSession session) {
		try {
			if(session.getAttribute("USER")!=null) {
				int sessionUserId = (int) session.getAttribute("USERID");
				if(sessionUserId!= postAuthorId && (boolean)session.getAttribute("isAdmin")==false) {
					return "redirect:/";
				}
				else {
					if(postDao.deletePost(postId)) {
						return "redirect:/"+postAuthorId;
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
			return "ErrorPage";
		}
	}

}
