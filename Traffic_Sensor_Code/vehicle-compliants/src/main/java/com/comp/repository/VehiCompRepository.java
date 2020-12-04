package com.comp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.comp.model.VehiComp;

public interface VehiCompRepository extends MongoRepository<VehiComp, String> {

}
