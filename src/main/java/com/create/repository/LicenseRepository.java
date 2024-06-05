package com.create.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.create.models.License;

public interface LicenseRepository extends JpaRepository<License, Integer>{

}
