package com.create.service;

import java.util.List;

import com.create.exception.DriverException;
import com.create.models.Driver;
import com.create.models.Ride;
import com.create.request.DriverSignUpRequest;

public interface DriverService {

	public Driver registerDriver(DriverSignUpRequest driverSignupRequest);
	
	public List<Driver> getAvailableDrivers(double pickupLatitude,double pickupLongitude, Ride ride);
	
	public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude);
	
	public Driver getReqDriverProfile(String jwt) throws DriverException;
	
	public Ride getDriverCurrentRide(Integer driverId) throws DriverException;
	
	public List<Ride> getAllocateRides(Integer driverId) throws DriverException;
	
	public Driver findDriverById(Integer driverId) throws DriverException;
	
	public List<Ride> completeRides(Integer driverId) throws DriverException;
 	
}
