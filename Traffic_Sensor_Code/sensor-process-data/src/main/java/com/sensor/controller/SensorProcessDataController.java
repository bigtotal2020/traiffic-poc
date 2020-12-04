package com.sensor.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sensor.service.SensorProcessDataService;




@RestController
@RequestMapping("/sensor")
public class SensorProcessDataController {
	
	@Value("${server.port}")
	int myPort;
	
	@Autowired DiscoveryClient client;
	
	@Autowired
	private LoadBalancerClient loadBalancer;
	
	@Autowired
	SensorProcessDataService dataService;
	
	RestTemplate restTemplate;
	
	@GetMapping("/check")
	public String check() {
		return "Sensor Process App working";
	}
	
	ResponseEntity<String> response=null;
	ResponseEntity<JSONObject> responseJSON=null;
	
	JSONParser jsonParser;
	
	@PostMapping("/process/data")
	public ResponseEntity<JSONObject> validate_owner_data(@RequestBody String ownerData) {
		
		jsonParser = new JSONParser();
		JSONObject obj1;
		ResponseEntity<JSONObject> sensorFinalResult = null;
		ResponseEntity<JSONObject> govRuleResponse = null;
		ResponseEntity<JSONObject> vehiRuleResponse = null;
		JSONObject sensorResult ; 
		try {
			obj1 = (JSONObject) jsonParser.parse(ownerData);
			govRuleResponse = fetchLoadBalancedAPIData("VALIDATE-GOVT-RULE", "/rule/govt/validate", ownerData);
			System.out.println("Sensor govRule result: " + govRuleResponse);
			
			vehiRuleResponse = fetchLoadBalancedAPIData("VALIDATE-VEHICLE-RULE", "/rule/vehi/validate", ownerData);
			System.out.println("Sensor vehiRule result: " + vehiRuleResponse);
			
			sensorResult = new JSONObject(govRuleResponse.getBody());
			sensorResult.putAll(vehiRuleResponse.getBody());
			
			System.out.println("Sensor Final result: " + sensorResult);
			sensorFinalResult = new ResponseEntity<>(sensorResult, HttpStatus.OK);
			//responseJSON = new ResponseEntity<>(vehiGovData.getBody(), HttpStatus.OK);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sensorFinalResult;
	}
	
	private ResponseEntity<JSONObject> fetchLoadBalancedAPIData(String service, String path, String data) {
		restTemplate = new RestTemplate();
		String baseUrl;
		try {
			
			// Good working - POST request with JSON object passed as body	
			ServiceInstance serviceInstance=loadBalancer.choose(service);
			
			System.out.println(serviceInstance.getUri());
			
			baseUrl=serviceInstance.getUri().toString() + path;
			URI uri = new URI(baseUrl);
			if (uri !=null ) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				HttpEntity<String> entity = new HttpEntity<String>(data, headers);
				responseJSON = restTemplate.postForEntity(uri, entity, JSONObject.class);
				
	      	}
	       
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return responseJSON;

	}
	
	private ResponseEntity<JSONObject> fetchAPIData(String service, String path, String data) {
		restTemplate = new RestTemplate();
		String baseUrl;
		try {
			
			// Good working - POST request with JSON object passed as body		
			List<ServiceInstance> list = client.getInstances(service);
	        if (list != null && list.size() > 0 ) {
	        	baseUrl = list.get(0).getUri().toString() + path;
	        	URI uri = new URI(baseUrl);
				if (uri !=null ) {
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);

					HttpEntity<String> entity = new HttpEntity<String>(data, headers);
					responseJSON = restTemplate.postForEntity(uri, entity, JSONObject.class);
					
		      	}
	       }
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return responseJSON;

	}
	
	private ResponseEntity<String> validateData(String service, String path) {
		restTemplate = new RestTemplate();
		ServiceInstance serviceInstance=loadBalancer.choose(service);
		
		System.out.println(serviceInstance.getUri());
		
		String baseUrl=serviceInstance.getUri().toString() + path;
		
		response = restTemplate.getForEntity(baseUrl, String.class);
		
		//System.out.println("Sensor app port is: " + myPort);
		return response;

	}

	@GetMapping("/restTemplate")
	public ResponseEntity<String> validate_using_rest_template() {
		String baseUrl = "http://localhost:8111/rule/govt/rule";
		restTemplate = new RestTemplate();
		
		try{
		
			// Step 1: Using [ hardcode URL + restTemplate.exchange ]
			//response = restTemplate.exchange(baseUrl, HttpMethod.GET, getHeaders(),String.class);
			
			// Step 2: Using [ hardcode URL + restTemplate.getForEntity ]
			//response = restTemplate.getForEntity(baseUrl, String.class);
			
			// Step 3: Using [ Eureka Discovery  + restTemplate.getForEntity ]
			List<ServiceInstance> list = client.getInstances("VALIDATE-GOVT-RULE");
	        if (list != null && list.size() > 0 ) {
	        	baseUrl = list.get(0).getUri().toString() + "/rule/govt/rule";
	        	URI uri = new URI(baseUrl);
	      	  	if (uri !=null ) {
	      	  	response = restTemplate.getForEntity(uri, String.class);
		      	}
	        }
			
		}catch (Exception ex)
		{
			System.out.println(ex);
		}
		System.out.println(response.getBody());
		return response;
		//return "Sensor Process App working";
	}
	
	@GetMapping("/loadBalancerRibbon")
	public ResponseEntity<String> validate_using_load_balancer() {
		restTemplate = new RestTemplate();
		ServiceInstance serviceInstance=loadBalancer.choose("VALIDATE-GOVT-RULE");
		
		System.out.println(serviceInstance.getUri());
		
		String baseUrl=serviceInstance.getUri().toString() + "/rule/govt/rule";
		
		response = restTemplate.getForEntity(baseUrl, String.class);
		
		System.out.println("Sensor app port is: " + myPort);
		return response;
	}
	
	@GetMapping("/loadDataFeign")
	public ResponseEntity<String> validate_using_feign() {
		response = dataService.getGovtRuleData();
		return response;
	}
	
	private static HttpEntity<?> getHeaders() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}
