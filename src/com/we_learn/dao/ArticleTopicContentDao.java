package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface ArticleTopicContentDao {
	JSONObject update(String param, String user_id);
	JSONObject insert(String param, String user_id);
	JSONObject delete(String param, String user_id);
	JSONObject remove(String param, String user_id);
}
