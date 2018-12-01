package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface TestDao {
	JSONObject insert(String param, String user_id);
	JSONObject update(String param, String user_id);
	JSONObject getById(String param);
	JSONObject getAll();
	JSONObject delete(String param, String user_id);
	JSONObject remove(String param, String user_id);
}
