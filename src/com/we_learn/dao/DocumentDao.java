package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface DocumentDao {
	JSONObject insert(String param, String user_id);
	JSONObject getAllDocument(String user_id);
}
