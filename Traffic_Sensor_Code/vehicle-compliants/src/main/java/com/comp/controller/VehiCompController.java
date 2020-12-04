package com.comp.controller;

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

import com.comp.model.VehiComp;
import com.comp.repository.VehiCompRepository;

@RestController
@RequestMapping("/complaint")
public class VehiCompController {

	@Autowired
	VehiCompRepository vehiCompRepository;
	
	@GetMapping("/check")
	public String check() {
		return "VehicleRule App working";
	}

	@GetMapping("/vehi")
	public ResponseEntity<VehiComp> getAllComplaints(@RequestParam(required = false) String title) {
		try {
			List<VehiComp> vehidata = new ArrayList<VehiComp>();

			vehiCompRepository.findAll().forEach(vehidata::add);
			
			if (vehidata.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(vehidata.get(0), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/vehi/{id}")
	public ResponseEntity<VehiComp> getComplaintById(@PathVariable("id") String id) {
		Optional<VehiComp> vehiData = vehiCompRepository.findById(id);

		if (vehiData.isPresent()) {
			return new ResponseEntity<>(vehiData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/vehi")
	public ResponseEntity<VehiComp> createComplaint(@RequestBody VehiComp comp) {
		try {
			VehiComp vehiD = vehiCompRepository
					.save(new VehiComp(comp.getVehicle_number(), comp.getReg_user_id(), comp.getFront_light(),
							comp.getBreak_light(), comp.getHundred_meters(), comp.getFifty_meters(),
							comp.getSeat_count(), comp.getHelmates()));
			return new ResponseEntity<>(vehiD, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
