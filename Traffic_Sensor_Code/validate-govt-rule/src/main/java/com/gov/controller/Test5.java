package com.gov.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Test5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject obj1 = new JSONObject();
		obj1.put("obA", "a1");
		obj1.put("obB", "a2");
		obj1.put("obC", "a3");
		
		JSONObject obj2 = new JSONObject();
		obj2.put("obA", "b1");
		obj2.put("obE", "b2");
		obj2.put("obF", "b3");
		
		JSONObject obj3 = new JSONObject(obj1);
		obj3.putAll(obj2);
		
		
		System.out.println(obj3);
	}

}
