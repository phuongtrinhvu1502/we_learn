package com.we_learn.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class ArticleTopicDaoImp implements ArticleTopicDao{
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
		String article_id = jsonParams.get("article_id").toString();
		String query = "INSERT INTO `article_topic`(`article_id`, `at_title`, `create_by`) VALUES (?,?,?)";
		try {
			this.jdbcTemplate.update(query, new Object[] {article_id, title, user_id});
		} catch (Exception e) {
			// TODO: handle exception
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article topic create success");
		return result;
	}

	@Override
	public JSONObject update(String param, String user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("title").toString();
		String at_id = jsonParams.get("at_id").toString();
		String query = "UPDATE `article_topic` SET `at_title`=?,`modify_date`=?,`modify_by`=? WHERE `at_id` = ?";
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			this.jdbcTemplate.update(query, new Object[] {title,dateTimeNow, user_id, at_id});
		} catch (Exception e) {
			// TODO: handle exception
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Article topic update success");
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

	@Override
	public JSONObject getTopicByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		StringBuilder builder = new StringBuilder();
		StringBuilder builderGetTotal = new StringBuilder();

		builder.append("SELECT at.at_title,DATE_FORMAT(at.created_date, '%d-%m-%Y') AS created_date FROM `article_topic` AS at "
				+ "LEFT JOIN `article` AS article ON at.article_id = article.article_id WHERE 1 = 1");
		builderGetTotal.append("SELECT COUNT(1) FROM article_topic AS at "
				+ "LEFT JOIN article AS article ON at.article_id = article.article_id ");
		// filter header
		if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
			builder.append(" AND at.deleted <> 1");
			builderGetTotal.append(" AND at.deleted <> 1");
		} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thùng rác
			builder.append(" AND at.deleted = 1");
			builderGetTotal.append(" AND at.deleted = 1");
		}
		if (Integer.parseInt(jsonParams.get("article_id").toString()) > -1) {
			builder.append(" AND article.article_id=" + jsonParams.get("type_id"));
			builderGetTotal.append(" AND article.article_id=" + jsonParams.get("type_id"));
		}
		if (jsonParams.get("at_title") != null && !"".equals(jsonParams.get("article_title").toString())) {
			builder.append(" AND at.at_title LIKE N'%" + jsonParams.get("article_title").toString() + "%'");
			builderGetTotal
					.append(" AND at.at_title LIKE N'%" + jsonParams.get("article_title").toString() + "%'");
		}
		// sortby
		if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
			switch (jsonParams.get("sortField").toString()) {
			default:
				builder.append(" ORDER BY created_date DESC");
				break;
			}
			// sortOrder chỉ là descend và ascend hoặc rỗng
			if (jsonParams.get("sortOrder") != null && "descend".equals(jsonParams.get("sortOrder").toString())) {
				builder.append(" DESC");
			}
			if (jsonParams.get("sortOrder") != null && "ascend".equals(jsonParams.get("sortOrder").toString())) {
				builder.append(" ASC");
			}
		}
		// lấy các biến từ table (limit, offset)
		mainUtil.getLimitOffset(builder, jsonParams);
		try {
			int totalRow = this.jdbcTemplate.queryForObject(builderGetTotal.toString(), Integer.class);
			List<Map<String, Object>> listArticle = this.jdbcTemplate.queryForList(builder.toString());
			JSONObject results = new JSONObject();
			results.put("results", listArticle);
			results.put("total", totalRow);
			data.put("data", results);
			data.put("success", true);
		} catch (Exception e) {
			data.put("success", false);
			data.put("err", e.getMessage());
			data.put("msg", "Lấy danh sách bài viết thất bại");
		}
		return data;
	}

	@Override
	public JSONObject getTopicById(String at_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		String query = "SELECT `at_id`, `at_title` FROM `article_topic` WHERE `at_id` = " + at_id;
		try {
			Map<String, Object> articleTopicObject = this.jdbcTemplate.queryForMap(query);
			result.put("success", true);
			result.put("data", articleTopicObject);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}
}
