package com.gov.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gov.model.Government;

public interface GovtRepository extends MongoRepository<Government, String> {
	
	List<Government> findByvehicleNumber(String vehicle_number);
	
}
