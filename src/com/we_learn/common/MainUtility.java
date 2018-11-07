package com.we_learn.common;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainUtility {
	public JSONObject stringToJson(String params) {
		JSONObject result = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			result = (JSONObject) parser.parse(params);
		} catch (ParseException e) {
			return null;
		}
		return result;
	}
}
