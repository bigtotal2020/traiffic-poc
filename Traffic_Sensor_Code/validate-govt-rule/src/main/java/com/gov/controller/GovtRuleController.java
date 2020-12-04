package com.gov.controller;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.gov.model.GovernmentRule;
import com.gov.repository.GovernmentRepository;

@RestController
@RequestMapping("/rule")
public class GovtRuleController {
	
	@Value("${server.port}")
	int myPort;
	
	@Autowired
	GovernmentRepository governmentRepository;
	
	@Autowired DiscoveryClient client;
	
	RestTemplate restTemplate;
	
	ResponseEntity<String> response=null;
	
	JSONParser jsonParser;
	
	JSONObject recvData;
	
	JSONObject vehiRuleResult;
	
	JSONArray complaintData;

	@GetMapping("/check")
	public String check() {
		return "Govt rule app is working";
	}
	
	@GetMapping("/govt/rule")
	public ResponseEntity<GovernmentRule> getAllGovtRules() {
		try {
			List<GovernmentRule> governmentRules = new ArrayList<GovernmentRule>();
			
			governmentRepository.findAll().forEach(governmentRules::add);

			if (governmentRules.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			governmentRules.forEach(el -> el.setPortNum(myPort));

			return new ResponseEntity<>(governmentRules.get(0), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/govt/rule/{id}")
	public ResponseEntity<GovernmentRule> getTutorialById(@PathVariable("id") String id) {
		Optional<GovernmentRule> governmentRule = governmentRepository.findById(id);

		if (governmentRule.isPresent()) {
			return new ResponseEntity<>(governmentRule.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/govt/rule")
	public ResponseEntity<GovernmentRule> createTutorial(@RequestBody GovernmentRule govtRule) {
		try {
			GovernmentRule governmentRule = governmentRepository
					.save(new GovernmentRule());
			return new ResponseEntity<>(governmentRule, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/govt/validate")
	public ResponseEntity<JSONObject> msg_to_owner(@RequestBody String data) throws ParseException {
		
		vehiRuleResult = new JSONObject();
		complaintData = new JSONArray();
		long daysDiff;
		String vehiNum;
		String vehiState;
		String sensed_date;
		System.out.println("Data is: " + data);
		//System.out.println(data);
		jsonParser = new JSONParser();
		recvData = (JSONObject) jsonParser.parse(data);
		System.out.println("jsonData is: " + recvData);
		sensed_date = (String) recvData.get("sensedDate");
		vehiNum = (String) recvData.get("vehicleNumber");
		vehiState = vehiNum.substring(0, 2);
		 
		String compElt = null;
		ResponseEntity<GovernmentRule> govtRuleEntity = getAllGovtRules();
		GovernmentRule jsonGovtRule = govtRuleEntity.getBody();
		
		ResponseEntity<String> vehiGovData = fetchAPIData("FETCH-VEHICLE-DATA-GOVT", "/fetch/govtdata", vehiNum);
		System.out.println("vehiGovData: " + vehiGovData);
		JSONObject jsonGovData = (JSONObject) jsonParser.parse(vehiGovData.getBody());
		Iterator<String> keys = recvData.keySet().iterator();
		System.out.println("jsonGovData - 1 : " + jsonGovData);
		
		System.out.println("jsonGovData date - 2: " + jsonGovData.get("ins_expiry_dt"));
				
		/*vehiGovData(vehiNum) = [vehicleNumber, 
			ins_ref_number, 
			ins_expiry_dt, 
			ems_ref_number, 
			ems_expiry_dt,
			sensed_date == vehicle_buy_dt]*/
		
		/*recvData - keys = [
		  	"vehicleNumber": "KA01A1234",
		    "break_light": true,
		    "seat_count": 2,
			"helmates": 2,
		    "sensedDate": "2020-12-04"
		  ]
		*/
		
		if(!vehiState.equalsIgnoreCase(jsonGovtRule.getVehicle_state())) {
			complaintData.add("Vehicle from different state should pay road tax");
		}
		
		while(keys.hasNext()) {
		    String key = keys.next();
		    System.out.println("Key: " + key + " and value: "+  recvData.get(key));
		    
		    //System.out.println("Vehi life: " + govtRule.getVehicle_life());
		    
		    
		    if(key.equalsIgnoreCase("sensedDate")) {
		    	String d1 = (String) jsonGovData.get("vehicle_buy_dt");
		    	String d2 = (String) jsonGovData.get("ins_expiry_dt");
		    	String d3 = (String) jsonGovData.get("ems_expiry_dt");
		    	// sensed_date
		    	daysDiff = getDiffDays(sensed_date, (String) jsonGovData.get("vehicle_buy_dt")); // vehicle_life
		    	if(daysDiff > jsonGovtRule.getVehicle_life()) {
		    		complaintData.add("Vehicle life is over. It is not permitted to drive");
		    	}
		    	System.out.println("daysDiff - vehicle_buy_dt : " + d1 + " & finalResult: " + daysDiff);
		    	
		    	daysDiff = getDiffDays(sensed_date, (String) jsonGovData.get("ins_expiry_dt")); // ins_expiry_dt
		    	if(daysDiff > jsonGovtRule.getIns_renew_window()) {
		    		complaintData.add("Vehicle insurance is expired. Renew early to avoid penalty");
		    	}
		    	System.out.println("daysDiff - ins_expiry_dt : " + d2 + " & finalResult: " + daysDiff);
		    	
		    	daysDiff = getDiffDays(sensed_date, (String) jsonGovData.get("ems_expiry_dt")); // ems_expiry_dt
		    	if(daysDiff > jsonGovtRule.getEms_renew_window()) {
		    		complaintData.add("Emission test is expired. Renewal is pending");
		    	}
		    	System.out.println("daysDiff - ems_expiry_dt : " + d3 + " & finalResult: " + daysDiff);
		    	
		    }
		    	
		    
		}
		
		vehiRuleResult.put("vehiNumber", (String) jsonGovData.get("vehicleNumber"));
		vehiRuleResult.put("vehiOwnerName", (String) jsonGovData.get("vehiOwnerName"));
		vehiRuleResult.put("vehiOwnerNumber", (long) jsonGovData.get("vehiOwnerNumber"));
		vehiRuleResult.put("govComplaintData", complaintData);
		vehiRuleResult.put("GovtRuleAPIPort", myPort);
		
		if(complaintData.size() > 0) {
			vehiRuleResult.put("govCompaints", complaintData.size());
		} else {
			vehiRuleResult.put("govCompaints", 0);
		}
		
		
		return new ResponseEntity<>(vehiRuleResult, HttpStatus.OK);
	}
	
	private ResponseEntity<String> fetchAPIData(String service, String path, String data) {
		restTemplate = new RestTemplate();
		String baseUrl;
		try {
			// Good working - GET request with data passed with URL
			List<ServiceInstance> list = client.getInstances(service);
	        if (list != null && list.size() > 0 ) {
	        	baseUrl = list.get(0).getUri().toString() + path + "/" + data;
	        	URI uri = new URI(baseUrl);
	      	  	if (uri !=null ) {
	      	  	response = restTemplate.getForEntity(uri, String.class);
		      	}
	        }
			
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return response;

	}
	
	
	private ResponseEntity<String> fetchAPIData_bkup(String service, String path, String data) {
		restTemplate = new RestTemplate();
		String baseUrl;
		try {
			List<ServiceInstance> list = client.getInstances(service);
	        if (list != null && list.size() > 0 ) {
	        	baseUrl = list.get(0).getUri().toString() + path;
	        	URI uri = new URI(baseUrl);
				if (uri !=null ) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);

					HttpEntity<String> entity = new HttpEntity<String>(data, headers);
					response = restTemplate.postForEntity(uri, entity, String.class);
	      	  		
		      	}
	        }
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return response;

	}
	
	private long getDiffDays(String now, String ref_dt) {
		
		LocalDate dateBefore = LocalDate.parse(ref_dt);
		LocalDate dateAfter = LocalDate.parse(now);
			
		//calculating number of days in between
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		return noOfDaysBetween;

	}
}
