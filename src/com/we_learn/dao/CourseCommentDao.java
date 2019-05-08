package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface CourseCommentDao {
	JSONObject insert(String param, String user_id);
	JSONObject update(String param, String user_id);
	JSONObject listCommentByPage(String param);
}
