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

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.LoginException;
import exceptions.UserException;

@Controller
@RequestMapping(value="/{id}")
public class ProfileController {
	private static final String LOGIN_REQUIRED_ERROR = "You need to be logged in to see this menu.";
	private static final Object ERROR_MESSAGE_FOR_INVALID_PAGE = "The page you are looking for doesn't exist or you don't have access";
	@Autowired
	private UserController userController;
	@Autowired
	private UserDao uDao;
	
	@RequestMapping(method =RequestMethod.GET, value = "/followers")
	public ModelAndView showFollowers(@PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			modelAndView.setViewName("Profile");
			if(session.getAttribute("USER")!=null) {
				try {
					List<User> followers = new LinkedList<>(uDao.getAllFollowers(id));
					modelAndView.addObject("users", followers);
					return modelAndView;
				} catch (UserException e) {
					e.printStackTrace();
					return new ModelAndView("ErrorPage" ,"errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
				}
			}
			modelAndView.addObject("error", LOGIN_REQUIRED_ERROR);
			return modelAndView;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage" ,"errorMessage", e.getMessage());
		}
	}
	
	@RequestMapping(method =RequestMethod.GET, value = "/followed")
	public ModelAndView showFollowed(@PathVariable Integer id, ModelAndView modelAndView, HttpSession session) {
		try {
			modelAndView.setViewName("Profile");
			if(session.getAttribute("USER")!=null) {
				try {
					List<User> followed = new LinkedList<>(uDao.getAllFollowedUsers(id));
					modelAndView.addObject("users", followed);
					return modelAndView;
				} catch (UserException e) {
					e.printStackTrace();
					return new ModelAndView("ErrorPage" ,"errorMessage", ERROR_MESSAGE_FOR_INVALID_PAGE);
				}
			}
			modelAndView.addObject("error", LOGIN_REQUIRED_ERROR);
			return modelAndView;
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ModelAndView("ErrorPage" ,"errorMessage", e.getMessage());
		}
	}
	@RequestMapping(value = "/follow", method = RequestMethod.POST)
	public String follow(@RequestParam("profileID") String profileID, HttpSession session, Model model) {
		try {
			if(session.getAttribute("USERID") !=null && profileID!=null) {
				int followerId = (int) session.getAttribute("USERID");
				int followedId = Integer.parseInt(profileID);
				try {
					if(uDao.checkIfFollowExistsInDb(followerId, followedId)) {
						uDao.unfollow(followerId, followedId);
						session.setAttribute("followedUser", followedId);
						session.setAttribute("followMessage", "Unfollowed");
						return "redirect:/"+profileID;
					}
					else {
						uDao.follow(followerId, followedId);
						session.setAttribute("followMessage", "Followed");
						return "redirect:/"+profileID;
					}
				} catch (UserException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "ErrorPage";
				}
			}
			session.setAttribute("error", LOGIN_REQUIRED_ERROR);
			return "redirect:/";
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
	@RequestMapping(method=RequestMethod.GET, value = "/editProfile")
	public String editProfile(@PathVariable int id, Model model, HttpSession session) {
		try {
			if(session.getAttribute("USER")!=null) {
				User u = uDao.getUserById(id);
				model.addAttribute("user", u);
				return "EditProfile";
			}
			else {
				session.setAttribute("error", LOGIN_REQUIRED_ERROR);
				return "redirect:/";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "ErrorPage";
		}
	}
	@RequestMapping(method=RequestMethod.POST, value = "/editProfile")
	public String editProfile(@PathVariable int id,@RequestParam(value = "new password", required = false) String newPass,
			@RequestParam(value = "new password2", required = false) String newPassConf,@RequestParam("email") String email,
			@RequestParam("old password") String pass, Model model, HttpSession session) {
				try {
					if(session.getAttribute("USER")!=null) {
						User u = uDao.getUserById(id);
						int id2 = uDao.login(u.getUsername(), pass);
						if(!email.equals(u.getEmail())) {
							if(uDao.checkIfEmailExistsInDB(email)) {
								model.addAttribute("message", "The email is already in use");
								return"EditProfile";
							}
						}
						if(id == id2) {
							if(newPass!=null && newPass.trim().length()>5) {
								if(newPassConf!=null && newPass.equals(newPassConf)) {
									if(uDao.editProfile(id,newPass,email)) {
										model.addAttribute("message", "You have successfully edited your profile");
										return"EditProfile";
									}
									else {
										model.addAttribute("message", "Something went wrong");
										return"EditProfile";
									}
								}
								else {
									model.addAttribute("message", "The passwords do not match");
									return"EditProfile";
								}
							}
							else {
								if(uDao.editProfile(id,pass,email)) {
									model.addAttribute("message", "You have successfully edited your profile");
									return"EditProfile";
								}
								else {
									model.addAttribute("message", "Something went wrong");
									return"EditProfile";
								}
							}

						}
						else {
							return"redirect:/";
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
					return "ErrorPage";
				}
	}
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProfile")
	public String deleteProfile(@PathVariable int id,@RequestParam("password") String password, Model model, HttpSession session) {
		try {
			if(session.getAttribute("USER")!=null) {
				int sessionId = (int) session.getAttribute("USERID");
				if(id != sessionId) {
					return"redirect:/";
				}
				try {
					User u = uDao.getUserById(id);
					uDao.login(u.getUsername(), password);
					if(uDao.deleteProfile(id)) {
						return userController.logOut(session, model);
					}
					else {
						model.addAttribute("message", "Something went wrong");
						return"EditProfile";
					}
					
				} catch (UserException | LoginException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", e.getMessage());
					return "ErrorPage";
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
			return "ErrorPage";
		}
	}
	
}
