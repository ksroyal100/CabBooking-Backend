package com.create.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.create.domain.RideStatus;
import com.create.exception.DriverException;
import com.create.exception.RideException;
import com.create.models.Driver;
import com.create.models.Ride;
import com.create.models.User;
import com.create.repository.DriverRepository;
import com.create.repository.RideRepository;
import com.create.request.RideRequest;

@Service
public class RideServiceImplementation implements RideService {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private RideRepository rideRepository;
	
	@Autowired
	private Calculators calculators;
	
	@Autowired
	private DriverRepository driverRepository;
	

	@Override
	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException {
		double pickupLatitude = rideRequest.getPickupLatitude();
		double pickupLongitude = rideRequest.getPickupLongitude();
		double destinationLatitude = rideRequest.getDestinationLatitude();
		double destinationLongitude = rideRequest.getDestinationLongitude();
		String pickupArea = rideRequest.getPickupArea();
		String destinationArea = rideRequest.getDestinationArea();
		
		Ride existRide = new Ride();
		List<Driver> availableDrivers = driverService.getAvailableDrivers(pickupLatitude, pickupLongitude,existRide);
		
		Driver nearestDriver = driverService.findNearestDriver(availableDrivers, pickupLatitude, pickupLongitude);
		
		if(nearestDriver==null) {
			throw new DriverException("Driver not available");
			
		}
		System.out.println("duration-----before ride");
		Ride ride = createRideRequest(user, nearestDriver, pickupLatitude, pickupLongitude, destinationLatitude, destinationLongitude, pickupArea, destinationArea);
		
		System.out.println("duration-----after ride");
		
		return ride;
	}

	@Override
	public Ride createRideRequest(User user, Driver nearestDriver, double pickupLatitude, double pickupLongitude,
			double destinationLatitude, double destinationLongitude, String pickupArea, String destinationArea) {
		Ride ride = new Ride();
		
		ride.setDriver(nearestDriver);
		ride.setUser(user);
		ride.setPickupLatitude(pickupLatitude);
		ride.setPickupLongitude(pickupLongitude);
		ride.setDestinationLatitude(destinationLatitude);
		ride.setDestinationLongitude(destinationLongitude);
		ride.setStatus(RideStatus.ACCEPTED);
		ride.setPickupArea(pickupArea);
		ride.setDestinationArea(destinationArea);
		
		System.out.println("---a-"+pickupLatitude);
		
		return rideRepository.save(ride);
	}

	@Override
	public void acceptRide(Integer rideId) throws RideException {
		
		Ride ride = findRideById(rideId);
		ride.setStatus(RideStatus.ACCEPTED);
		
		Driver driver = ride.getDriver();
		
		driver.setCurrentRide(ride);
		
		Random random = new Random();
		
		int otp= random.nextInt() + 100;
		ride.setOtp(otp);
		
		driverRepository.save(driver);
		rideRepository.save(ride);
	}

	@Override
	public void declineRide(Integer rideId, Integer driverId) throws RideException {
		Ride ride = new Ride();
		System.out.println(ride.getId());
		
		ride.getDeclinedDrivers().add(driverId);
		
		System.out.println(ride.getId()+"-"+ride.getDeclinedDrivers());
		
		List<Driver> availableDrivers = driverService.getAvailableDrivers(ride.getPickupLatitude(),ride.getPickupLongitude(),ride);
		
		Driver nearestDriver = driverService.findNearestDriver(availableDrivers, ride.getPickupLatitude(), ride.getPickupLongitude());
		
		ride.setDriver(nearestDriver);
		
		rideRepository.save(ride);
	}

	@Override
	public void startRide(Integer rideId, int otp) throws RideException {
		Ride ride = findRideById(rideId);
		if(otp!=ride.getOtp()) {
			throw new RideException("please provide a valid otp");
		}
		ride.setStatus(RideStatus.STARTED);
		ride.setStartTime(LocalDateTime.now());
		rideRepository.save(ride);
		
	}

	@Override
	public void completeRide(Integer rideId) throws RideException {
		Ride ride = findRideById(rideId);
		
		ride.setStatus(RideStatus.COMPLETED);
		ride.setEndTime(LocalDateTime.now());
		
		double distance = calculators.calculateDistance(ride.getDestinationLatitude(), ride.getDestinationLatitude(), ride.getPickupLatitude(), ride.getPickupLongitude());
		
		LocalDateTime start = ride.getStartTime();
		LocalDateTime end = ride.getEndTime();
		Duration duration = Duration.between(start, end);
		long miliSecond = duration.toMillis();
		
		System.out.println("duration------"+ miliSecond);
		double fare = calculators.calculateFare(distance);
		
		ride.setDistance(Math.round(distance*100.0)/100.0);
		ride.setFare((int)Math.round(fare));
		ride.setDuration(miliSecond);
		ride.setEndTime(LocalDateTime.now());
		
		Driver driver = ride.getDriver();
		driver.getRides().add(ride);
		driver.setCurrentRide(null);
		
		Integer driverRevenue = (int)(driver.getTotalRevenue()+Math.round(fare*0.8));
		System.out.println("driver revenue--"+ driverRevenue);
		
		driverRepository.save(driver);
		rideRepository.save(ride);
	}

	@Override
	public void cancelRide(Integer rideId) throws RideException {
		Ride ride = findRideById(rideId);
		ride.setStatus(RideStatus.CANCELLED);
		rideRepository.save(ride);
		
	}

	@Override
	public Ride findRideById(Integer rideId) throws RideException {
		Optional<Ride> ride = rideRepository.findById(rideId);
		
		if(ride.isPresent()) {
			return ride.get();
		}
		throw new RideException("ride not exist with id"+ rideId);
		
	}

}
