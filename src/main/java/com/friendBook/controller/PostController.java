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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.UserDao;

import exceptions.PostException;
import exceptions.UserException;

@Controller
public class PostController {

	@Autowired
	private PostDao postDao;
	
	@Autowired
	private UserDao uDao;
	
	@RequestMapping(method=RequestMethod.GET, value="/posts")
	public String extractFeed(Model model, HttpServletRequest request) throws PostException {
		HttpSession session = request.getSession();
		if(session.getAttribute("USERID")!=null) {
			int userId = (int) session.getAttribute("USERID");
			try {
				List<Integer> followedUsersIds = new LinkedList<>(uDao.getAllFollowedUsers(userId));
				Set<Post> feed = new TreeSet<>();
				for(Integer i : followedUsersIds) {
					feed.addAll(postDao.extractPosts(i));
				}
				model.addAttribute("posts", feed);
				return "index.jsp";
			} catch (UserException e) {
				return "index.jsp";
			}
		} 
		return "index.jsp";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/Profile.jsp")
	public String extractUserPosts(Model model, HttpServletRequest request, @RequestParam("id") String id) throws PostException {
		List <Post> postsList = new LinkedList<>(postDao.extractPosts(Integer.parseInt(id)));
		model.addAttribute("posts", postsList);
		return "Profile.jsp";
	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	public String publish(Model model, HttpServletRequest request) {
		
		String text = request.getParameter("postText");
		String pictureUrl = request.getParameter("pictureUrl");
		String picture = extractPictureName(pictureUrl);		
		
		try {			
			if (!((text == null || text.length() == 0) 
					&& (picture == null || picture.length() == 0))) {	
				HttpSession session = request.getSession();
				int userId = (int) session.getAttribute("USERID");
				
				Post newPost = new Post(0, userId);
				newPost.setText(text);
				newPost.setPictureUrl(picture);
				postDao.publish(newPost);	
				model.addAttribute("post", newPost);
				return extractUserPosts(model, request, ""+userId);
			}
			return "redirect:ErrorForm.html";
		} catch (PostException e) {
			e.printStackTrace();
			return "redirect:ErrorForm.html";
		}
	
	}
	
	
	//TODO da se napravqt pyrvo v PostDAO
	public void delete() throws PostException {

	}

	public void edit() throws PostException {

	}
	
	private String extractPictureName(String pictureUrl) {
		if(pictureUrl.length() > 0) {
			int symb = pictureUrl.length()-1;
			for(;symb>0;symb--) {
				if(pictureUrl.charAt(symb) == '/' || pictureUrl.charAt(symb) == '\\') {
					symb++;
					break;
				}
			}
			return pictureUrl.substring(symb);
		}
		return "";
	}

}
