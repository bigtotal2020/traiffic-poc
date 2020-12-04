package com.gov.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "govt_data")
public class Government {
	
	@Id
    private String id;
	
	//@Field ("vehiclenumber")
	String vehicleNumber;
	int ins_ref_number;
	String ins_expiry_dt;
	int ems_ref_number;
	String ems_expiry_dt;
	String vehicle_buy_dt;
	String vehiOwnerName;
	long vehiOwnerNumber;
	

	public Government() {
		// TODO Auto-generated constructor stub
	}

	
	
	public Government(String vehicleNumber, int ins_ref_number, String ins_expiry_dt, int ems_ref_number,
			String ems_expiry_dt, String vehicle_buy_dt, String vehiOwnerName, long vehiOwnerNumber) {
		this.vehicleNumber = vehicleNumber;
		this.ins_ref_number = ins_ref_number;
		this.ins_expiry_dt = ins_expiry_dt;
		this.ems_ref_number = ems_ref_number;
		this.ems_expiry_dt = ems_expiry_dt;
		this.vehicle_buy_dt = vehicle_buy_dt;
		this.vehiOwnerName = vehiOwnerName;
		this.vehiOwnerNumber = vehiOwnerNumber;
	}
	
	

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getVehicleNumber() {
		return vehicleNumber;
	}


	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}


	public int getIns_ref_number() {
		return ins_ref_number;
	}


	public void setIns_ref_number(int ins_ref_number) {
		this.ins_ref_number = ins_ref_number;
	}


	public String getIns_expiry_dt() {
		return ins_expiry_dt;
	}


	public void setIns_expiry_dt(String ins_expiry_dt) {
		this.ins_expiry_dt = ins_expiry_dt;
	}


	public int getEms_ref_number() {
		return ems_ref_number;
	}


	public void setEms_ref_number(int ems_ref_number) {
		this.ems_ref_number = ems_ref_number;
	}


	public String getEms_expiry_dt() {
		return ems_expiry_dt;
	}


	public void setEms_expiry_dt(String ems_expiry_dt) {
		this.ems_expiry_dt = ems_expiry_dt;
	}


	public String getVehicle_buy_dt() {
		return vehicle_buy_dt;
	}


	public void setVehicle_buy_dt(String vehicle_buy_dt) {
		this.vehicle_buy_dt = vehicle_buy_dt;
	}


	public String getVehiOwnerName() {
		return vehiOwnerName;
	}


	public void setVehiOwnerName(String vehiOwnerName) {
		this.vehiOwnerName = vehiOwnerName;
	}


	public long getVehiOwnerNumber() {
		return vehiOwnerNumber;
	}


	public void setVehiOwnerNumber(long vehiOwnerNumber) {
		this.vehiOwnerNumber = vehiOwnerNumber;
	}
	
	
	
	
	
}
