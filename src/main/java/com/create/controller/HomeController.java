package com.create.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create.response.messageResponse;

@RestController
public class HomeController {

	@GetMapping("/")
	public ResponseEntity<messageResponse> homeController(){
		messageResponse msg= new messageResponse("Welcome Ola Backend System");
		return new ResponseEntity<messageResponse>(msg,HttpStatus.OK);
	}
}
