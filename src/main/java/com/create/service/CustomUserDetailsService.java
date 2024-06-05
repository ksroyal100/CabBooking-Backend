package com.create.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.create.models.Driver;
import com.create.models.User;
import com.create.repository.DriverRepository;
import com.create.repository.UserRepository;

@Service

public class CustomUserDetailsService implements UserDetailsService {

private DriverRepository driverRepository;
private UserRepository userRepository;

public CustomUserDetailsService(DriverRepository driverRepository, UserRepository userRepository) {
	this.driverRepository = driverRepository;
	this.userRepository = userRepository;
	
}

@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
	
	List<GrantedAuthority> authorities = new ArrayList<>();
	
	User user = userRepository.findByEmail(username);
	
	if(user !=null) {
		return new org.springframework.security.core.userdetails
				.User(user.getEmail(),user.getPassword(),authorities);
	}
	Driver driver = driverRepository.findByEmail(username);
	if(driver!=null) {
		return new org.springframework.security.core.userdetails
				.User(user.getEmail(),user.getPassword(),authorities);
	}
	throw new UsernameNotFoundException("user not found with this - "+username);
	

}
}
