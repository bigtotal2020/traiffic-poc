package com.vehicle.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vehi_rules")
public class VehiRule {
	
	@Id
    private String id;
	
	boolean front_light;
	boolean break_light;
	double hundred_meters;
	double fifty_meters;
	int seat_count;
	int helmates;
	
	public VehiRule() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public VehiRule(boolean front_light, boolean break_light, double hundred_meters, double fifty_meters,
			int seat_count, int helmates) {
		this.front_light = front_light;
		this.break_light = break_light;
		this.hundred_meters = hundred_meters;
		this.fifty_meters = fifty_meters;
		this.seat_count = seat_count;
		this.helmates = helmates;
	}


	public boolean isFront_light() {
		return front_light;
	}

	public void setFront_light(boolean front_light) {
		this.front_light = front_light;
	}
	public boolean isBreak_light() {
		return break_light;
	}

	public void setBreak_light(boolean break_light) {
		this.break_light = break_light;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public double getHundred_meters() {
		return hundred_meters;
	}
	public void setHundred_meters(double hundred_meters) {
		this.hundred_meters = hundred_meters;
	}
	public double getFifty_meters() {
		return fifty_meters;
	}
	public void setFifty_meters(double fifty_meters) {
		this.fifty_meters = fifty_meters;
	}
	public int getSeat_count() {
		return seat_count;
	}
	public void setSeat_count(int seat_count) {
		this.seat_count = seat_count;
	}
	public int getHelmates() {
		return helmates;
	}
	public void setHelmates(int helmates) {
		this.helmates = helmates;
	}
	
	
}
