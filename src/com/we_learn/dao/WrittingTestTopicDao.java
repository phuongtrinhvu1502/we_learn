package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface WrittingTestTopicDao {
	JSONObject insert(String param, String user_id);
	JSONObject getTopicByPage(String param);
	JSONObject getById(String wtt_id);
}
