package com.coderscampus.assignment13.web;

import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/register")
	public String getCreateUser (ModelMap model) {
		
		model.put("user", new User());
		
		return "register";
	}
	
	@PostMapping("/register")
	public String postCreateUser (User user) {
		System.out.println(user);
		userService.saveUser(user);
		return "redirect:/users";
	}
	
	@GetMapping("/users")
	public String getAllUsers (ModelMap model) {
		Set<User> users = userService.findAll();
		
		model.put("users", users);
		if (users.size() == 1) {
			model.put("user", users.iterator().next());
		}
		
		return "users";
	}
	
	@GetMapping("/user_details/{userId}")
	public String getOneUser (ModelMap model, @PathVariable Long userId) {
		User user = userService.findById(userId);
		var showUpdateForm = "true";
		if(user != null) {
		model.put("users", Arrays.asList(user));
		model.put("user", user);
		model.addAttribute("showUpdateForm", showUpdateForm != null ? showUpdateForm : false);
		return "user_details";
	} else {
		return "redirect:/users";
	}
}

@PostMapping("/user_details/{userId}/update")
public String updateUserDetailsAndAddress(@PathVariable Long userId, User user, BindingResult result, ModelMap model) {
    if (result.hasErrors()) {
        // Handle errors, perhaps returning to the form with validation messages
        return "user_details"; // Assuming 'user_details' is your form view
    }
    
    // Optional: Fetch the existing user to merge updates if necessary
    User existingUser = userService.findById(userId);
    if (existingUser == null) {
        // Handle case where user does not exist
        return "redirect:/users";
    }
	// Assuming 'user' contains the updated details and associated address from the form
    // You might need to merge 'user' with 'existingUser' depending on how your form is set up
    userService.updateUserAndAddress(user); // Call the new service method
    
    return "redirect:/user_details/" + userId; 
}
	@PostMapping("/user_details/{userId}")
	public String postOneUser (User user) {
		userService.saveUser(user);
		return "redirect:/users/"+user.getUserId();
	}
	
	@PostMapping("/users_details/{userId}/delete")
	public String deleteOneUser (@PathVariable Long userId) {
		userService.delete(userId);
		return "redirect:/users";
	}
}
