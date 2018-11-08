package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface TopicDao {
	JSONObject insert(String param, String user_id);
	JSONObject update(String param, String user_id);
	JSONObject delete(String param, String user_id);
	JSONObject getTopicByPage(String param);
}
