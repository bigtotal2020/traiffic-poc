package com.gov.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gov.model.GovernmentRule;

public interface GovernmentRepository extends MongoRepository<GovernmentRule, String> {

}
