package com.create.service;

import com.create.exception.DriverException;
import com.create.exception.RideException;
import com.create.models.Driver;
import com.create.models.Ride;
import com.create.models.User;
import com.create.request.RideRequest;

public interface RideService {

	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;
	
	public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude,double pickupLongitude,
			double destinationLatitude,double destinationLongitude, String pickupArea, String destinationArea);
	public void acceptRide(Integer rideId) throws RideException;
	public void declineRide(Integer rideId,Integer driverId) throws RideException;
	public void startRide(Integer rideId, int opt) throws RideException;
	public void completeRide(Integer rideId) throws RideException;
	public void cancelRide(Integer rideId) throws RideException;
	public Ride findRideById(Integer rideId) throws RideException;
}
