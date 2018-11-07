package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface LoginDao {
	public JSONObject login(String params);
	public JSONObject logout(String token);
}
