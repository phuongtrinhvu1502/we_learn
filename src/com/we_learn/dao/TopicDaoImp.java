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
		String title = jsonParams.get("article_title").toString();
		String content = jsonParams.get("content").toString();
		String type = jsonParams.get("article_type").toString();
		
		String query = "INSERT INTO `article`(`article_title`, `article_content`, `created_by``, `type_id`) VALUE (?,?,?,?)";
		//insert
		try {
			Object[] objects = new Object[] {title, content, user_id, type};
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
		String title = jsonParams.get("article_title").toString();
		String content = jsonParams.get("content").toString();
		String type = jsonParams.get("article_type").toString();
		String article_id = jsonParams.get("article_id").toString();
		String query = "UPDATE `topic` SET `article_title`=?, `article_content` =?,`type_id` = ?, `modify_date` =?, `modify_by` = ? WHERE `article_id` = ?";
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
		String query = "UPDATE `article` SET `deleted`=1, `modify_date` =?, `modify_by` = ? WHERE `article_id` = ?";
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
		String query = "SELECT `article`.`created_by`,`article`.`article_title`, `article`.`modify_date`, `crm_user`.`full_name`, "
				+ "if(a.`comments` IS NULL, 0 , a.`comments`) AS comments FROM `article` "
				+ "LEFT JOIN `crm_user` ON `crm_user`.`user_id` = `article`.`created_by` "
				+ "LEFT JOIN (SELECT COUNT(1) AS `comments`, `article_id` "
				+ "FROM `article_comment` GROUP BY `article_comment`.`article_id`) AS a ON a.`article_id` = `article`.`article_id` "
				+ "WHERE `article`.`article_title` LIKE '" + key + "%' ORDER BY ? ? LIMIT ?,10";
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
