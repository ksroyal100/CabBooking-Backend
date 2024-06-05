package com.create.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.create.dto.RideDTO;
import com.create.dto.mapper.DTOmapper;
import com.create.exception.DriverException;
import com.create.exception.RideException;
import com.create.exception.UserException;
import com.create.models.Driver;
import com.create.models.Ride;
import com.create.models.User;
import com.create.request.RideRequest;
import com.create.request.StartRideRequest;
import com.create.response.messageResponse;
import com.create.service.DriverService;
import com.create.service.RideService;
import com.create.service.UserService;

@RestController
@RequestMapping("/api/rides")
public class RideController {

	@Autowired
	private RideService rideService;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/request")
	public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization") String jwt) throws DriverException, UserException{
	User user= userService.getReqUserProfile(jwt);
	Ride ride= rideService.requestRide(rideRequest, user);
	RideDTO rideDto = DTOmapper.toRideDto(ride);
	return new ResponseEntity<>(rideDto,HttpStatus.ACCEPTED);
} 
    @PutMapping("/{rideId}/accept")
    public ResponseEntity<messageResponse> acceptRideHandler(@PathVariable Integer rideId) throws UserException, RideException{
    	rideService.acceptRide(rideId);
    	messageResponse res = new messageResponse("Ride Accepted By Driver");
    	return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }
    
    @PutMapping("/{rideId}/decline")
    public ResponseEntity<messageResponse>  declineRideHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer rideId) throws UserException,RideException,DriverException{
		
    	Driver driver = driverService.getReqDriverProfile(jwt);
    	
    	rideService.declineRide(rideId, driver.getId());
    	
    	messageResponse res = new messageResponse("Ride decline By Driver");
    	return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    	
    }
    
    @PutMapping("/{rideId}/start")
    public ResponseEntity<messageResponse> rideStartHandler(@PathVariable Integer rideId, @RequestBody StartRideRequest req) throws UserException, DriverException, RideException{
    	rideService.startRide(rideId, req.getOtp());
    	messageResponse res = new messageResponse("Ride is Started");
    	return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    	
    }
    @PutMapping("/{ride}/complete")
    public ResponseEntity<messageResponse> rideCompleteHandler(@PathVariable Integer rideId) throws UserException,RideException{
    	rideService.completeRide(rideId);
    	messageResponse res = new messageResponse("Ride is Completed Thank You For Booking Cab");
    	return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
    @GetMapping("/{rideId}")
   public ResponseEntity<RideDTO> findRideByIdHandler(@PathVariable Integer rideId, @RequestHeader("Authorization") String jwt) throws UserException, RideException {
    	User user = userService.getReqUserProfile(jwt);
    	Ride ride = rideService.findRideById(rideId);
    	
    	RideDTO rideDto = DTOmapper.toRideDto(ride);
    	
    	
    	return new ResponseEntity<RideDTO>(rideDto,HttpStatus.ACCEPTED);
    }
    
}
