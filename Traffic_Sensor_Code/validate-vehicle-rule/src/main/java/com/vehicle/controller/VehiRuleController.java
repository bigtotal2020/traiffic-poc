package com.vehicle.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.vehicle.model.VehiRule;
import com.vehicle.repository.VehiRuleRepository;


@RestController
@RequestMapping("/rule")
public class VehiRuleController {
	
	@Value("${server.port}")
	int myPort;

	@Autowired
	VehiRuleRepository vehiRuleRepository;
	
	JSONParser jsonParser;
	
	JSONObject recvVehiData;
	
	JSONObject vehiRuleResult;
	
	JSONArray complaintData;
	
	@GetMapping("/check")
	public String check() {
		return "VehicleRule App working";
	}

	@GetMapping("/vehi/rule")
	//@HystrixCommand(fallbackMethod = "getAllVehiRulesFallbackMethod")
	public ResponseEntity<VehiRule> getAllVehiRules() {
		try {
			List<VehiRule> vehidata = new ArrayList<VehiRule>();

			vehiRuleRepository.findAll().forEach(vehidata::add);
			
			if (vehidata.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(vehidata.get(0), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/vehi/validate")
	public ResponseEntity<JSONObject> vehiRuleValidate(@RequestBody String data) throws ParseException {
		
		vehiRuleResult = new JSONObject();
		complaintData = new JSONArray();
		jsonParser = new JSONParser();
		recvVehiData = (JSONObject) jsonParser.parse(data);
		
		ResponseEntity<VehiRule> vehiRuleEntity = getAllVehiRules();
		VehiRule jsonVehiRule = vehiRuleEntity.getBody();
		Iterator<String> keys = recvVehiData.keySet().iterator();
		
		System.out.println("recvVehiData: " + recvVehiData);
		//System.out.println("recvVehiData: " + recvVehiData);
		
		while(keys.hasNext()) {
		    String key = keys.next();
		    
		    if(key.equalsIgnoreCase("front_light")) {
		    	int flight = Boolean.compare((Boolean) recvVehiData.get("front_light"), (Boolean) jsonVehiRule.isFront_light());
		    	if(flight != 0)
		    		complaintData.add("Front light is not functioning. It should be fixed");
		    	
		    } else if(key.equalsIgnoreCase("break_light")) {
		    	int blight = Boolean.compare((Boolean) recvVehiData.get("break_light"), (Boolean) jsonVehiRule.isBreak_light());
		    	if(blight != 0)
		    		complaintData.add("Break light is not functioning. It should be fixed");
		    	
		    } else if(key.equalsIgnoreCase("hundred_meters")) {
		    	if((double) recvVehiData.get("hundred_meters") > jsonVehiRule.getHundred_meters())
		    		complaintData.add("Over speed near 100 meters of the signal");
		    	
		    } else if(key.equalsIgnoreCase("fifty_meters")) {
		    	if((double) recvVehiData.get("fifty_meters") > jsonVehiRule.getFifty_meters())
		    		complaintData.add("Over speed near 50 meters of the signal");
		    	
		    } else if(key.equalsIgnoreCase("seat_count")) {
		    	if((long) recvVehiData.get("seat_count") > jsonVehiRule.getSeat_count())
		    		complaintData.add("Your bike sitting capacity is crossed. Max of " + jsonVehiRule.getSeat_count() + " is allowed to travel in bike" );
		    	
		    } else if(key.equalsIgnoreCase("helmates")) {
		    	if((long) recvVehiData.get("helmates") > jsonVehiRule.getHelmates())
		    		complaintData.add("Your bike travelers helmate wearing count is less than " +  jsonVehiRule.getHelmates());
		    	
		    }
		}
		
		vehiRuleResult.put("vehiNumber", (String) recvVehiData.get("vehicleNumber"));
		vehiRuleResult.put("vehiComplaintData", complaintData);
		vehiRuleResult.put("VehiRuleAPIPort", myPort);
		
		if(complaintData.size() > 0) {
			vehiRuleResult.put("vehiCompaints", complaintData.size());
		} else {
			vehiRuleResult.put("vehiCompaints", 0);
		}
		
		return new ResponseEntity<>(vehiRuleResult, HttpStatus.OK);
		
	}
	
	public String getAllVehiRulesFallbackMethod(@RequestParam(required = false) String title) {
		return "Currently, vehicle rules data source is not avaiable";
	}

	@GetMapping("/vehi/rule/{id}")
	public ResponseEntity<VehiRule> getVehiRuleById(@PathVariable("id") String id) {
		Optional<VehiRule> vehiData = vehiRuleRepository.findById(id);

		if (vehiData.isPresent()) {
			return new ResponseEntity<>(vehiData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/vehi/rule")
	public ResponseEntity<VehiRule> createVehiRule(@RequestBody VehiRule vehi) {
		try {
			VehiRule vehiD = vehiRuleRepository
					.save(new VehiRule(vehi.isFront_light(), vehi.isBreak_light(), vehi.getHundred_meters(),
							vehi.getFifty_meters(), vehi.getSeat_count(), vehi.getHelmates()));
			return new ResponseEntity<>(vehiD, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
