package com.friendBook.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.friendBook.model.Post;
import com.friendBook.model.PostDao;
import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.LoginException;
import exceptions.PostException;
import exceptions.UserException;

@Controller
@RequestMapping(value="/{id}")
public class ProfileController {
	private static final String NO_ACCESS = "You don't have access to this menu";
	private static final String PASSWORD_MISSMATCH_ERROR = "The passwords do not match";
	private static final String UNEXPECTED_ERROR = "Something went wrong";
	private static final String SUCESSFULL_PROFILE_EDIT_MESSAGE = "You have successfully edited your profile";
	private static final String EMAIL_DUPLICATE_ERROR = "The email is already in use";
	private static final String LOGIN_REQUIRED_ERROR = "You need to be logged in to see this menu.";
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";
	private static final Object WRONG_PASSWORD_ERROR = "Wrong password";
	@Autowired
	private UserController userController;
	@Autowired
	private UserDao uDao;
	@Autowired
	private PostDao postDao;
	
	
	
	@RequestMapping(method =RequestMethod.GET)
	public ModelAndView showProfile(@RequestParam(value = "show", required = false) String show, @PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			try {		
				modelAndView.setViewName("Profile");
				if (uDao.checkIfUserExistsInDB(id)) {
					if(checkIfFollowed(session, id))
						modelAndView.addObject("isFollowed", true);
					else
						modelAndView.addObject("isFollowed", false);
					if(show!=null) {
						if(session.getAttribute("user")!=null) {
							switch(show) {
								case "followed":
									List<User> followed = new LinkedList<>(uDao.getAllFollowedUsers(id));
									modelAndView.addObject("users", followed);
									modelAndView.setViewName("Profile");
									return modelAndView;
								case "followers":
									List<User> followers = new LinkedList<>(uDao.getAllFollowers(id));
									modelAndView.addObject("users", followers);
									return modelAndView;
	
							}
						}
						else {
							modelAndView.addObject("accessError", LOGIN_REQUIRED_ERROR);
						}
					}
					List<Post> postsList = new LinkedList<>(postDao.extractPosts(id));
					modelAndView.addObject("posts", postsList);
					modelAndView.addObject("id", id);
					return modelAndView;
				} else
					return new ModelAndView("error", "errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			} catch (UserException | PostException e) {
				e.printStackTrace();
				e.printStackTrace();
				return new ModelAndView("error", "errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("error", "errorMessage", e.getMessage());
		}

	}
	
	
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public String follow(@RequestParam("profileID") int profileID, HttpSession session, Model model) {
		try {
			if(session.getAttribute("user") !=null && profileID>0) {
				User user = (User) session.getAttribute("user");
				int followerId = user.getId();
				try {
					if(uDao.checkIfFollowExistsInDb(followerId, profileID)) {
						uDao.unfollow(followerId, profileID);
						session.setAttribute("followedUser", profileID);
						session.setAttribute("followMessage", "Unfollowed");
						return "redirect:/"+profileID;
					}
					else {
						uDao.follow(followerId, profileID);
						session.setAttribute("followMessage", "Followed");
						return "redirect:/"+profileID;
					}
				} catch (UserException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";
				}
			}
			session.setAttribute("error", LOGIN_REQUIRED_ERROR);
			return "redirect:/";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(method=RequestMethod.GET, value = "/editProfile")
	public String editProfile(@PathVariable int id, Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")!=null) {
				User user = (User) session.getAttribute("user");
				int sessionUserId = user.getId();
				if(sessionUserId == id)
					return "EditProfile";
				else {
					session.setAttribute("errorMessage", NO_ACCESS);
					return "redirect:/error";
				}
					
			}
			else {
				session.setAttribute("error", LOGIN_REQUIRED_ERROR);
				return "redirect:/";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	@RequestMapping(method=RequestMethod.POST, value = "/editProfile")
	public String editProfile(@PathVariable int id,@RequestParam(value = "new password", required = false) String newPass,
			@RequestParam(value = "new password2", required = false) String newPassConf,@RequestParam("email") String email,
			@RequestParam("old password") String pass, Model model, HttpSession session) {
				try {
					if(session.getAttribute("user")!=null) {
						try {
							User u = uDao.getUserById(id);
							User u2 = uDao.login(u.getUsername(), pass);
							User user3 =(User) session.getAttribute("user");
							if(!email.equals(u.getEmail())) {
								if(uDao.checkIfEmailExistsInDB(email)) {
									model.addAttribute("message", EMAIL_DUPLICATE_ERROR);
									return"EditProfile";
								}
							}
							if(id == u2.getId()) {
								if(newPass!=null && newPass.trim().length()>5) {
									if(newPassConf!=null && newPass.equals(newPassConf)) {
										if(uDao.editProfile(id,newPass,email)) {
											user3.setEmail(email);
											session.setAttribute("user", user3);
											model.addAttribute("message", SUCESSFULL_PROFILE_EDIT_MESSAGE);
											return"EditProfile";
										}
										else {
											model.addAttribute("message", UNEXPECTED_ERROR);
											return"EditProfile";
										}
									}
									else {
										model.addAttribute("message", PASSWORD_MISSMATCH_ERROR);
										return"EditProfile";
									}
								}
								else {
									if(uDao.editProfile(id,pass,email)) {
										
										user3.setEmail(email);
										session.setAttribute("user", user3);
										model.addAttribute("message", SUCESSFULL_PROFILE_EDIT_MESSAGE);
										return"EditProfile";
									}
									else {
										model.addAttribute("message", UNEXPECTED_ERROR);
										return"EditProfile";
									}
								}
	
							}
							else {
								return"redirect:/";
							}
						}
						catch(LoginException e) {
							model.addAttribute("message", WRONG_PASSWORD_ERROR);
							return"EditProfile";
						}
					}
					else {
						session.setAttribute("error", LOGIN_REQUIRED_ERROR);
						return "redirect:/";
					}
				}
				catch(Exception e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";
				}
	}
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProfile")
	public String deleteProfile(@PathVariable int id,@RequestParam("password") String password, Model model, HttpSession session) {
		try {
			if(session.getAttribute("user")!=null) {
				User user = (User) session.getAttribute("user");
				int sessionUserId = user.getId();
				if(id != sessionUserId) {
					return"redirect:/";
				}
				try {
					User u = uDao.getUserById(id);
					uDao.login(u.getUsername(), password);
					if(uDao.deleteProfile(id)) {
						return userController.logOut(session, model);
					}
					else {
						model.addAttribute("message", UNEXPECTED_ERROR);
						return"EditProfile";
					}
					
				} catch (UserException | LoginException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "error";
				}
			}
			else {
				session.setAttribute("error", LOGIN_REQUIRED_ERROR);
				return "redirect:/";
			}
		}
		catch(Exception e){
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}
	private boolean checkIfFollowed(HttpSession session, int followedId) throws UserException {
		if(session.getAttribute("user")!=null) {
			User user = (User) session.getAttribute("user");
			int followerId = user.getId();
			if(uDao.checkIfFollowExistsInDb(followerId, followedId))
				return true;
			else
				return false;
					
		}
		return false;
	}
}
