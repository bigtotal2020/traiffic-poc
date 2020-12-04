package com.comp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehi_complaints")
public class VehiComp {

	@Id
    private String id;
	
	String vehicle_number;
	int reg_user_id;
	String front_light;
	String break_light;
	String hundred_meters;
	String fifty_meters;
	String seat_count;
	String helmates;
		
	public VehiComp() {
	}
	
	

	public VehiComp(String vehicle_number, int reg_user_id, String front_light, String break_light,
			String hundred_meters, String fifty_meters, String seat_count, String helmates) {
		this.vehicle_number = vehicle_number;
		this.reg_user_id = reg_user_id;
		this.front_light = front_light;
		this.break_light = break_light;
		this.hundred_meters = hundred_meters;
		this.fifty_meters = fifty_meters;
		this.seat_count = seat_count;
		this.helmates = helmates;
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVehicle_number() {
		return vehicle_number;
	}
	public void setVehicle_number(String vehicle_number) {
		this.vehicle_number = vehicle_number;
	}
	public int getReg_user_id() {
		return reg_user_id;
	}
	public void setReg_user_id(int reg_user_id) {
		this.reg_user_id = reg_user_id;
	}
	public String getFront_light() {
		return front_light;
	}
	public void setFront_light(String front_light) {
		this.front_light = front_light;
	}
	public String getBreak_light() {
		return break_light;
	}
	public void setBreak_light(String break_light) {
		this.break_light = break_light;
	}
	public String getHundred_meters() {
		return hundred_meters;
	}
	public void setHundred_meters(String hundred_meters) {
		this.hundred_meters = hundred_meters;
	}
	public String getFifty_meters() {
		return fifty_meters;
	}
	public void setFifty_meters(String fifty_meters) {
		this.fifty_meters = fifty_meters;
	}
	public String getSeat_count() {
		return seat_count;
	}
	public void setSeat_count(String seat_count) {
		this.seat_count = seat_count;
	}
	public String getHelmates() {
		return helmates;
	}
	public void setHelmates(String helmates) {
		this.helmates = helmates;
	}
	
	
	
	
	
	
}
