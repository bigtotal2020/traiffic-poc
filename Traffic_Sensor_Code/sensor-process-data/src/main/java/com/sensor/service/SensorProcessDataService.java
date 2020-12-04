package com.sensor.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="VALIDATE-GOVT-RULE")
public interface SensorProcessDataService {

	@RequestMapping(method=RequestMethod.GET, value="/rule/govt/rule")
	public ResponseEntity<String> getGovtRuleData();
}
