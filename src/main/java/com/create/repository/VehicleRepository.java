package com.create.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.create.models.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer>{

}
