package com.vehicle.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vehicle.model.VehiRule;

public interface VehiRuleRepository  extends MongoRepository<VehiRule, String> {

}
