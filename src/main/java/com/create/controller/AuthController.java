package com.create.controller;


import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create.config.JwtUtil;
import com.create.domain.UserRole;
import com.create.exception.DriverException;
import com.create.exception.UserException;
import com.create.models.Driver;
import com.create.models.User;
import com.create.repository.DriverRepository;
import com.create.repository.UserRepository;
import com.create.request.DriverSignUpRequest;
import com.create.request.LoginRequest;
import com.create.request.SignupRequest;
import com.create.response.JwtResponse;
import com.create.service.CustomUserDetailsService;
import com.create.service.DriverService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private UserRepository userRepository;
	private DriverRepository driverRepository;
	private PasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;
	private CustomUserDetailsService customUSerDetailService;
	private DriverService driverService;
	
	public AuthController(UserRepository userRepository, DriverRepository driverRepository,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtil,DriverService driverService, CustomUserDetailsService customUSerDetailService) {
		this.userRepository = userRepository;
		this.driverRepository = driverRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.driverService = driverService;
		this.customUSerDetailService = customUSerDetailService;
		
		
	}
	
	@PostMapping("/user/signup")
	public ResponseEntity<JwtResponse> signupHandler(@RequestBody SignupRequest req) throws UserException{
		
		String email = req.getEmail();
		String fullName= req.getFullName();
		String mobile= req.getMobile();
		String password= req.getPassword();
		
		User user= userRepository.findByEmail(email);
		
		if(user!=null) {
			throw new UserException("User Already Exist with email"+ email);
		}
		
		String encodedPassword = passwordEncoder.encode(password);
		
		User createUser = new User();
		createUser.setEmail(email);
		createUser.setPassword(encodedPassword);
		createUser.setFullName(fullName);
		createUser.setMobile(mobile);
		createUser.setRole(UserRole.USER);
		
		User savedUser = userRepository.save(createUser);
		
		Authentication authentication =new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtil.generateJwtToken(authentication);
				
		JwtResponse JwtResponse = new JwtResponse();
		JwtResponse.setJwt(jwt);
		JwtResponse.setAuthentication(true);
		JwtResponse.setError(false);
		JwtResponse.setErrorDetails(null);
		JwtResponse.setType(UserRole.USER);
		JwtResponse.setMessage("Account Created Successfully: "+ savedUser.getFullName());
		
		
		return new ResponseEntity<JwtResponse>(JwtResponse, HttpStatus.OK);
		
	}
	
	@PostMapping("/driver/signup")
	public ResponseEntity<JwtResponse> driverSignupHandler(@RequestBody DriverSignUpRequest driverSignupRequest) throws DriverException{
		
		Driver driver = driverRepository.findByEmail(driverSignupRequest.getEmail());
		
		JwtResponse JwtResponse = new JwtResponse();
		
		if(driver!=null) {
			JwtResponse.setAuthentication(false);
			JwtResponse.setErrorDetails("Email Already Exist");
			JwtResponse.setError(true);
			
			return new ResponseEntity<JwtResponse>(JwtResponse, HttpStatus.BAD_REQUEST);
		}
		
		Driver createdDriver = driverService.registerDriver(driverSignupRequest);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(createdDriver.getEmail(), createdDriver.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtil.generateJwtToken(authentication);
		
		JwtResponse.setJwt(jwt);
		JwtResponse.setAuthentication(true);
		JwtResponse.setError(false);
		JwtResponse.setErrorDetails(null);
		JwtResponse.setType(UserRole.USER);
		JwtResponse.setMessage("Account Created Successfully: "+createdDriver.getName());
		
		
		
		return new ResponseEntity<JwtResponse>(JwtResponse, HttpStatus.OK);
		
	}
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> signin(@RequestBody LoginRequest req){
		String username = req.getEmail();
		String password = req.getPassword();
		Authentication authentication = authenticate(password, username);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtil.generateJwtToken(authentication);
		
		JwtResponse JwtResponse = new JwtResponse();
		JwtResponse.setJwt(jwt);
		JwtResponse.setAuthentication(true);
		JwtResponse.setError(false);
		JwtResponse.setErrorDetails(null);
		JwtResponse.setType(UserRole.USER);
		JwtResponse.setMessage("Account LoggedIn Successfully");
		
		return new ResponseEntity<JwtResponse>(JwtResponse, HttpStatus.ACCEPTED);
	}
	
	private Authentication authenticate(String password, String username) {
		UserDetails userDetails = customUSerDetailService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid Username or password");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
	}
}
