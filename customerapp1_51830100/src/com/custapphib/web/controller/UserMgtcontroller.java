package com.custapphib.web.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.custapphib.model.persistence.User;
import com.custapphib.model.persistence.UserNotFoundException;
import com.custapphib.service.UserService;



@Controller
public class UserMgtcontroller {
	@Autowired
	private UserService userservice;

	//@PostConstruct
	public void init() {
		userservice.addUser(new User("Krishna", "k@gmail.com", "kP", "admin", true));
		userservice.addUser(new User("Vyshnavi", "v@gmail", "v123", "mgr", true));
		userservice.addUser(new User("P", "p@gmail", "p123", "trainee", true));
	}

	@RequestMapping(value = "login")
	public String loginGet(HttpServletRequest req, ModelMap map) {
		map.addAttribute("userbean", new UserFormBean());
		map.addAttribute("error", req.getParameter("error"));
		return "login";

	}
	@RequestMapping(value = "logout")
	public String logout(ModelMap map) {
		map.addAttribute("userbean", new UserFormBean());
		map.addAttribute("message", "you are successfully logged out!");
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest req, @Valid UserFormBean userbean, BindingResult bindingResult) {
		User user = null;
		if (bindingResult.hasErrors()) {
			return "login";
		} else {
			try {
				user = userservice.findUser(userbean.getEmail(), userbean.getPassword());
			} catch (UserNotFoundException e) {
				return "redirect:login?error=login failed";

			}

		}
		HttpSession httpSession = req.getSession();
		httpSession.setAttribute("user", user);
		return "redirect:allcustomer";

	}
	
}
