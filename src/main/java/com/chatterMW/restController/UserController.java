package com.chatterMW.restController;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatter.DAO.UserDAO;
import com.chatter.model.User;

@RestController
public class UserController {

	@Autowired
	UserDAO userDAO;

	// ----------------- Add User ---------------
	@PostMapping(value = "/registerUser")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		user.setAccountOpeningDate(new Date());
		user.setRole("USER");
		user.setEnabled(true);
		if (userDAO.insertUser(user)) {
			return new ResponseEntity<String>("User Registered Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User registration failed", HttpStatus.NOT_FOUND);
		}
	}

	// ----------- Update User -----------------------------
	@PutMapping(value = "/updateUser/{userId}")
	public ResponseEntity<String> updateUser(@PathVariable("userId") int userId, @RequestBody User user) {
		System.out.println("In updating user " + userId);
		User mUser = userDAO.getUser(userId);
		if (mUser == null) {
			System.out.println("No user found with Id " + userId);
			return new ResponseEntity<String>("No user found", HttpStatus.NOT_FOUND);
		}

		mUser.setEmail(user.getEmail());
		mUser.setPhone(user.getPhone());
		mUser.setAddress(user.getAddress());

		userDAO.updateUser(mUser);
		return new ResponseEntity<String>("User updated successfully", HttpStatus.OK);
	}

	// --------------------- List Users --------------------------
	@GetMapping(value = "/listUsers")
	public ResponseEntity<List<User>> listUsers() {
		List<User> listUsers = userDAO.listUsers();
		if (listUsers.size() != 0) {
			return new ResponseEntity<List<User>>(listUsers, HttpStatus.OK);
		}
		return new ResponseEntity<List<User>>(listUsers, HttpStatus.NOT_FOUND);
	}

	// --------------------- Get User ----------------------
	@GetMapping(value = "/getUser/{userId}")
	public ResponseEntity<String> getUser(@PathVariable("userId") int userId) {
		User user = userDAO.getUser(userId);
		if (user == null) {
			System.out.println("No user found");
			return new ResponseEntity<String>("User with Id " + userId + " not found", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>("User with Id " + userId + " found", HttpStatus.OK);
		}
	}

	@DeleteMapping(value = "/deleteUser/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") int userId) {
		System.out.println("In delete user" + userId);
		User user = userDAO.getUser(userId);
		if (user == null) {
			System.out.println("No user with Id " + userId + " found to delete");
			return new ResponseEntity<String>("No user found to delete", HttpStatus.NOT_FOUND);
		} else {
			userDAO.deleteUser(user);
			return new ResponseEntity<String>("User with Id " + userId + " deleted successfully", HttpStatus.OK);
		}
	}

}
