package com.we_learn.dao;

import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class TopicDaoImp implements TopicDao{
	private JdbcTemplate jdbcTemplate;

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
		String title = jsonParams.get("topic_title").toString();
		String content = jsonParams.get("content").toString();
		String type = jsonParams.get("topic_type").toString();
		
		String query = "INSERT INTO `topic`(`user_id`, `article_title`, `article_content`, `create_by`, `update_by`, `type_id`) VALUE (?,?,?,?,?,?)";
		//insert
		try {
			Object[] objects = new Object[] {user_id, title, content, user_id, user_id, type};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article create success");
		return result;
	}

	@Override
	public JSONObject update(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject delete(String param) {
		// TODO Auto-generated method stub
		return null;
	}

}
