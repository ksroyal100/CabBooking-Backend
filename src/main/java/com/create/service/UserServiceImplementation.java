package com.create.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.create.config.JwtUtil;
import com.create.exception.UserException;
import com.create.models.Ride;
import com.create.models.User;
import com.create.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;


	@Override
	public User getReqUserProfile(String token) throws UserException {
		String email = jwtUtil.getEmailFromToken(token);
		User user = userRepository.findByEmail(email);
		
		if(user!=null) {
			return user;
		}
		throw new UserException("invalid token...");
	}

	@Override
	public User findUserById(Integer Id) throws UserException {
		Optional<User> opt = userRepository.findById(Id);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new UserException("user not found with id"+Id);
		
	}


	@Override
	public List<Ride> completedRide(Integer userId) throws UserException {
		List<Ride> completedRides = userRepository.getCompletedRides(userId);
		return completedRides;
	}
	
}
