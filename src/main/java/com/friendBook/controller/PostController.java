package com.friendBook.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;

import exceptions.PostException;

@Controller
public class PostController {

	@Autowired
	private PostDao postDao;
	
	@RequestMapping(method=RequestMethod.GET, value="/posts")
	public String extractPosts(Model model, HttpServletRequest request) throws PostException {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("USERID");
		List <Post> postsList = postDao.extractPosts(userId);
		model.addAttribute("posts", postsList);
		return "index.jsp";
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
			}
			return extractPosts(model, request);
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
