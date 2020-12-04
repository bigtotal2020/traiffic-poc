package com.gov.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gov.model.Government;
import com.gov.repository.GovtRepository;

@RestController
@RequestMapping("/fetch")
public class GovtDataController {

	@Autowired
	GovtRepository govtRepository;
	
	@GetMapping("/check")
	public String check() {
		return "Govt Data App working";
	}

	@GetMapping("/govtdata")
	public ResponseEntity<List<Government>> getAllVehiGovData(@RequestParam(required = false) String title) {
		try {
			List<Government> govtdata = new ArrayList<Government>();

			govtRepository.findAll().forEach(govtdata::add);
			
			if (govtdata.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(govtdata, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/govtdata/{vehiNum}")
	public ResponseEntity<Government> getVehiGovDataById(@PathVariable("vehiNum") String vehiNum) {
		System.out.println("Vehicle Number: " + vehiNum);
		List<Government> vehiGovData = govtRepository.findByvehicleNumber(vehiNum);

		if (vehiGovData.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(vehiGovData.get(0), HttpStatus.OK);
	}

	@PostMapping("/govtdata")
	public ResponseEntity<Government> createVehiGovData(@RequestBody Government govt) {
		try {
			Government govtD = govtRepository
					.save(new Government(govt.getVehicleNumber(), govt.getIns_ref_number(), govt.getIns_expiry_dt(),
							govt.getEms_ref_number(), govt.getEms_expiry_dt(), govt.getVehicle_buy_dt(),
							govt.getVehiOwnerName(), govt.getVehiOwnerNumber()));
			return new ResponseEntity<>(govtD, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
