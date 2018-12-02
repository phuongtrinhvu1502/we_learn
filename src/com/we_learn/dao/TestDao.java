package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface TestDao {
	JSONObject insert(String param, String user_id);
	public JSONObject getTestByPage(String article_id);
	JSONObject getTestById(String param);
}
