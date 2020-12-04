package com.notify.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notify.model.Owner;

@RestController
@RequestMapping("/message")
public class NotifyMessageController {
	
	@Value("${mail-address}") String govt_address;
	
	@GetMapping("/check")
	public String check() {
		return "Messaging is working";
	}
	
	@PostMapping("/owner")
	public String msg_to_owner(@RequestBody Owner owner) {
		return "Complaint is delivered to registered owner " + owner.getName() + " of vehicle number " + owner.getVehicleNumber();
	}
	
	@PostMapping("/govt")
	public String msg_to_govt(@RequestBody String complaint) {
		return "Complaint is delivered to govt " + govt_address + " of vehicle number " + complaint;
	}
}
