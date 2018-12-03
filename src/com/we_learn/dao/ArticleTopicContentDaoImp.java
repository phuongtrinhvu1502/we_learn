package com.we_learn.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class ArticleTopicContentDaoImp implements ArticleTopicContentDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(CreateTestDaoImpl.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject insert(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("title").toString();
		String at_id = jsonParams.get("at_id").toString();
		String content = jsonParams.get("content").toString();
		String query = "INSERT INTO `article_topic_content`(`at_id`, `atc_title`, `atc_content`,`created_by`) VALUES (?,?,?,?)";
		try {
			this.jdbcTemplate.update(query, new Object[] {at_id, title, content, user_id});
		} catch (Exception e) {
			// TODO: handle exception
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article topic content create success");
		return result;
	}

	@Override
	public JSONObject update(String param, String user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("title").toString();
		String act_id = jsonParams.get("act_id").toString();
		String content = jsonParams.get("content").toString();
		String query = "UPDATE `article_topic_content` SET `atc_title`=?,`atc_content`=?,`modify_date`=?,`modify_by`=? WHERE `atc_id` = ?";
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			this.jdbcTemplate.update(query, new Object[] {title, content,dateTimeNow, user_id, act_id});
		} catch (Exception e) {
			// TODO: handle exception
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article topic content update success");
		return result;
	}

	@Override
	public JSONObject delete(String param, String user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject remove(String param, String user_id) {
		// TODO Auto-generated method stub
		return null;
	}
}
