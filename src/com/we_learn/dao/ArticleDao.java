package com.we_learn.dao;

import org.json.simple.JSONObject;

public interface ArticleDao {
	JSONObject insert(String param, String user_id);
	JSONObject update(String param, String user_id);
	JSONObject delete(String article, int user_id);
	JSONObject remove(String article, int user_id);
	JSONObject restore(String article, int user_id);
	JSONObject getListArticleByType(String param);
	JSONObject getArticleByPage(String param);
	JSONObject getArticleById(String param);
	JSONObject getAllListArticle();
}
