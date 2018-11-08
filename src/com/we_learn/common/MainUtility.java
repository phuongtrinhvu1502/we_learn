package com.we_learn.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	public String getDateFormat(String format, Date date) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.format(date);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
}
