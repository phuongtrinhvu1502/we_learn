package com.we_learn.dao;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.json.simple.JSONObject;

public interface CourseDao {
	JSONObject insert(String param, String user_id, InputStream video, String location);
	JSONObject update(String param, String user_id);
	public JSONObject delete(String qa, int user_id);
	public JSONObject remove(String qa, int user_id);
	public JSONObject restore(String qa, int user_id);
	public JSONObject getCourseById(String qa_id);
	public JSONObject viewCourseById(String qa_id, String user_id);
	JSONObject getCourseByPage(String param);
}
