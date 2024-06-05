package com.create.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.create.config.JwtUtil;
import com.create.domain.RideStatus;
import com.create.domain.UserRole;
import com.create.exception.DriverException;
import com.create.models.Driver;
import com.create.models.License;
import com.create.models.Ride;
import com.create.models.Vehicle;
import com.create.repository.DriverRepository;
import com.create.repository.LicenseRepository;
import com.create.repository.RideRepository;
import com.create.repository.VehicleRepository;
import com.create.request.DriverSignUpRequest;

@Service
public class DriverServiceImplementation implements DriverService{

	@Autowired
	private DriverRepository driverRepository;
	@Autowired
	private Calculators distanceCalculator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private VehicleRepository vehicleRepository;
	@Autowired
	private LicenseRepository licenseRepository;
	@Autowired
	private RideRepository rideRepository;
	
	
	
	@Override
	public Driver registerDriver(DriverSignUpRequest driverSignupRequest) {
		License license= driverSignupRequest.getLicense();
		Vehicle vehicle= driverSignupRequest.getVehicle();
		
		License createdLicense = new License();
		
		createdLicense.setLicenseState(license.getLicenseState());
		createdLicense.setLicenseNumber(license.getLicenseNumber());
		createdLicense.setLicenseExpirationDate(license.getLicenseExpirationDate());
		createdLicense.setId(license.getId());
		
		License savedLicense = licenseRepository.save(createdLicense);
		
		
		Vehicle createdVehicle = new Vehicle();
		
		createdVehicle.setCapacity(vehicle.getCapacity());
		createdVehicle.setColor(vehicle.getColor());
		createdVehicle.setId(vehicle.getId());
		createdVehicle.setLicensePlate(vehicle.getLicensePlate());
		createdVehicle.setMake(vehicle.getMake());
		createdVehicle.setModel(vehicle.getModel());
		createdVehicle.setYear(vehicle.getYear());
		
		Vehicle savedVehicle = vehicleRepository.save(createdVehicle);
		
		Driver driver = new Driver();
		String encodePassword = passwordEncoder.encode(driverSignupRequest.getPassword());
		
		driver.setEmail(driverSignupRequest.getEmail());
		driver.setName(driverSignupRequest.getName());
		driver.setMobile(driverSignupRequest.getMobile());
		driver.setPassword(encodePassword);
		driver.setLicense(savedLicense);
		driver.setVehicle(savedVehicle);
		driver.setRole(UserRole.DRIVER);
		
		driver.setLatitude(driverSignupRequest.getLatitude());
		driver.setLongitude(driverSignupRequest.getLongitude());
		
		Driver createdDriver = driverRepository.save(driver);
		
		savedLicense.setDriver(createdDriver);
		savedVehicle.setDriver(createdDriver);
		
		licenseRepository.save(savedLicense);
		vehicleRepository.save(savedVehicle);
		
		return createdDriver;
		
	}
	
	@Override
	public List<Driver> getAvailableDrivers(double pickupLatitude, double pickupLongitude, Ride ride) {
		List<Driver> allDrivers = driverRepository.findAll();
		List<Driver> availableDriver = new ArrayList<>();
		
		for(Driver driver:allDrivers) {
			if(driver.getCurrentRide()!=null && driver.getCurrentRide().getStatus()!= RideStatus.COMPLETED) {
				continue;
			}
			if(ride.getDeclinedDrivers().contains(driver.getId())) {
				System.out.println("its containes");
				continue;
			}
			double driverLatitude = driver.getLatitude();
			double driverLongitude = driver.getLongitude();
			
			double distance = distanceCalculator.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);
				availableDriver.add(driver);
		}
		return availableDriver;
	}
	@Override
	public Driver findNearestDriver(List<Driver> availableDrivers, double pickupLatitude, double pickupLongitude) {
		double min= Double.MAX_VALUE;
		Driver nearestDriver = null;
		
		for(Driver driver : availableDrivers) {
			double driverLatitude = driver.getLatitude();
			double driverLongitude = driver.getLongitude();
			
			double distance = distanceCalculator.calculateDistance(driverLatitude, driverLongitude, pickupLatitude, pickupLongitude);
			
			if(min>distance) {
				min=distance;
				nearestDriver=driver;
			}
		}
		return nearestDriver;
	}
	@Override
	public Driver getReqDriverProfile(String jwt) throws DriverException {
		String email = jwtUtil.getEmailFromToken(jwt);
		Driver driver = driverRepository.findByEmail(email);
		if(driver==null) {
			throw new DriverException("driver not exist with email:");
		}
		return driver;
	}
	@Override
	public Ride getDriverCurrentRide(Integer driverId) throws DriverException {
		Driver driver = findDriverById(driverId);
		return driver.getCurrentRide();
	}
	@Override
	public List<Ride> getAllocateRides(Integer driverId) throws DriverException {
		List<Ride> allocateRides = driverRepository.getAllocatedRides(driverId);
		return allocateRides;
	}
	@Override
	public Driver findDriverById(Integer driverId) throws DriverException {
		Optional<Driver> opt= driverRepository.findById(driverId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new DriverException("driver not exist");
	}
	@Override
	public List<Ride> completeRides(Integer driverId) throws DriverException {
		List<Ride> completedRides = driverRepository.getCompletedRides(driverId);
		return completedRides;
	}
	
}
