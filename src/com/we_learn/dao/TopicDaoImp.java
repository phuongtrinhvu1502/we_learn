package com.we_learn.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public JSONObject update(String param, String user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("topic_title").toString();
		String content = jsonParams.get("content").toString();
		String type = jsonParams.get("topic_type").toString();
		String article_id = jsonParams.get("article_id").toString();
		String query = "UPDATE `topic` SET `article_title`=?, `article_content` =?,`type_id` = ?, `update_at` =?, `update_by` = ? WHERE `article_id` = ?";
		//insert
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			Object[] objects = new Object[] {title, content, type, dateTimeNow, user_id, article_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article update success");
		return result;
	}

	@Override
	public JSONObject delete(String param, String user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String article_id = jsonParams.get("article_id").toString();
		String query = "UPDATE `topic` SET `deleted`=1, `update_at` =?, `update_by` = ? WHERE `article_id` = ?";
		//insert
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			Object[] objects = new Object[] {dateTimeNow, user_id, article_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article delete success");
		return result;
	}

	@Override
	public JSONObject getTopicByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String page = jsonParams.get("page").toString();
		int index = mainUtil.getPageIndex(page);
		String order = jsonParams.get("order").toString();
		String orderBy = jsonParams.get("orderBy").toString();
		String key = jsonParams.get("key").toString();
		List<Map<String, Object>> listTopic = new ArrayList<>();
		String query = "SELECT `topic`.`user_id`,`topic`.`article_title`, `topic`.`create_at`, `crm_user`.`full_name`, "
				+ "if(a.`comments` IS NULL, 0 , a.`comments`) AS comments FROM `topic` "
				+ "LEFT JOIN `crm_user` ON `crm_user`.`user_id` = `topic`.`user_id` "
				+ "LEFT JOIN (SELECT COUNT(1) AS `comments`, `article_id` "
				+ "FROM `article_comment` GROUP BY `article_comment`.`article_id`) AS a ON a.`article_id` = `topic`.`article_id` "
				+ "WHERE `topic`.`article_title` LIKE '" + key + "%' ORDER BY ? ? LIMIT ?,10";
		try {
			Object[] objects = new Object[] {order, orderBy, index};
			listTopic = this.jdbcTemplate.queryForList(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		result.put("success", true);
		result.put("list", listTopic);
		return result;
	}

}
