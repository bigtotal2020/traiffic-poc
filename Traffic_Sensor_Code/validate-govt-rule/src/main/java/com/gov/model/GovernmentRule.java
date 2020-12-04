package com.gov.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "govt_rules")
public class GovernmentRule {
	
	@Id
    private String id;
	
	int vehicle_life;
	String vehicle_state;
	int ins_renew_window;
	int ems_renew_window;
	
	int portNum;
	
	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}


	public GovernmentRule() {
		// TODO Auto-generated constructor stub
	}
	
	

	public GovernmentRule(int vehicle_life, String vehicle_state, int ins_renew_window, int ems_renew_window) {
		super();
		this.vehicle_life = vehicle_life;
		this.vehicle_state = vehicle_state;
		this.ins_renew_window = ins_renew_window;
		this.ems_renew_window = ems_renew_window;
	}

	public int getVehicle_life() {
		return vehicle_life;
	}

	public void setVehicle_life(int vehicle_life) {
		this.vehicle_life = vehicle_life;
	}

	public String getVehicle_state() {
		return vehicle_state;
	}

	public void setVehicle_state(String vehicle_state) {
		this.vehicle_state = vehicle_state;
	}

	public int getIns_renew_window() {
		return ins_renew_window;
	}

	public void setIns_renew_window(int ins_renew_window) {
		this.ins_renew_window = ins_renew_window;
	}

	public int getEms_renew_window() {
		return ems_renew_window;
	}

	public void setEms_renew_window(int ems_renew_window) {
		this.ems_renew_window = ems_renew_window;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
	
}
