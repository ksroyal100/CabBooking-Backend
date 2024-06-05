package com.create.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create.exception.UserException;
import com.create.models.Ride;
import com.create.models.User;
import com.create.service.UserService;

@RestController
@RequestMapping("/api/uses")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<User> findUserByIdHanlder(@PathVariable Integer userId)throws UserException{
		System.out.println("find by user id");
		User createdUser = userService.findUserById(userId);
		
		return new ResponseEntity<User>(createdUser,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<User> getReqUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		
		User user = userService.getReqUserProfile(jwt);
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
		
	}
	
	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getCompletedRideHandler(@RequestHeader("Authorization") String jwt) throws UserException{
		User user = userService.getReqUserProfile(jwt);
		List<Ride> rides = userService.completedRide(user.getId());
		
		return new ResponseEntity<>(rides,HttpStatus.ACCEPTED);
		
	}
	
	
	
}
