package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface TopicDao {
	JSONObject insert(String param, String user_id);
	JSONObject update(String param, String user_id);
	public JSONObject delete(String article, int user_id);
	public JSONObject remove(String article, int user_id);
	public JSONObject restore(String article, int user_id);
	public JSONObject getArticleById(String article_id);
	JSONObject getTopicByPage(String param);
	JSONObject getCommentByArticle(String param);
}
