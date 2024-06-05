package com.create.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.create.models.Ride;;

public interface RideRepository extends JpaRepository<Ride, Integer>{

	
}
