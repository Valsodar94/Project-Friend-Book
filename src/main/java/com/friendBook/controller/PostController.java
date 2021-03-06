package com.friendBook.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.PostException;
import exceptions.UserException;

@javax.servlet.annotation.MultipartConfig
@Controller
public class PostController {
	private static final String ERROR_MESSAGE_FOR_EMPTY_POST = "You can't publish an empty post. Write a text or upload a picture.";

	// constants
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";
	private static final String SESSION_EXPIRED_MESSAGE = "Your session has expired. You need to login";

	private static final String UPLOADED_FOLDER = "C://pictures//";
	@Autowired
	private PostDao postDao;

	@Autowired
	private UserDao uDao;
    
//	@RequestMapping
//	public String fallbackMethod(){
//		return "redirect:/index";
//	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public String publish(@RequestParam("pictureUrl") MultipartFile file,@RequestParam("tags") String tags, Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			response.setContentType("text/html; charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("utf-8");

			if (session.getAttribute("user") != null) {
				String text = request.getParameter("postText");
				System.out.println("[DEBUG] PostController .publish " + text);
				String picture = savePicture(file);

				try {
					if (!((text == null || text.length() == 0) && (picture == null || picture.length() == 0))) {

						User user = (User) session.getAttribute("user");
						int userId = user.getId();
						Post newPost = new Post(0, userId);
						newPost.setText(text);
						newPost.setPictureUrl(picture);
						newPost.setTags(tags);
						postDao.publish(newPost);
						model.addAttribute("post", newPost);
						return "redirect:/" + userId;
					}
					model.addAttribute("errorMessage", ERROR_MESSAGE_FOR_EMPTY_POST);
					return "error";
				} catch (PostException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";
				}
			} else {
				session.setAttribute("error", SESSION_EXPIRED_MESSAGE);
				return "redirect:/";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}

	public String savePicture(MultipartFile file) throws PostException {
	       if (file.isEmpty()) {
	            return null;
	        }
        try {
        	List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
        	if(!contentTypes.contains(file.getContentType())) {
        		return null;
        	}
    		byte[] bytes = file.getBytes();
    		String pictureName = file.getOriginalFilename();
            Path path = Paths.get(UPLOADED_FOLDER +pictureName);
			Files.write(path, bytes);
			return pictureName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
       

	}
	
	
	@RequestMapping(value = "/deletePost", method = RequestMethod.POST)
	public String deletePost(@RequestParam("postId")int postId, @RequestParam("postAuthorId")int postAuthorId, Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")!=null) {
				User user = (User) session.getAttribute("user");
				int sessionUserId = user.getId();
				if(sessionUserId!= postAuthorId && user.isAdmin()==false) {
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
			return "error";
		}
	}

}
