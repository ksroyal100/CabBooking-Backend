package com.create.service;

import java.util.List;

import com.create.exception.UserException;
import com.create.models.Ride;
import com.create.models.User;

public interface UserService {

//	public User createUser(User user) throws UserException;
	
	public User getReqUserProfile(String token) throws UserException;
	
	public User findUserById(Integer Id) throws UserException;
	
//	public User findUserByEmail(String email) throws UserException;
	
//	public User findUserByToken(String token) throws UserException;
	
	public List<Ride> completedRide(Integer userId) throws UserException;
	
	
}
