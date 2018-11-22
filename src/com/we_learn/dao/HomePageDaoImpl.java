package com.we_learn.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.dao.HomePageDaoImpl;
import com.we_learn.common.MainUtility;

public class HomePageDaoImpl implements HomePageDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(HomePageDaoImpl.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject getNewestByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		StringBuilder builder = new StringBuilder();
		StringBuilder builderGetTotal = new StringBuilder();
		
		builder.append(
				"SELECT article.article_id, article.article_title, type.article_type_name, "
						+ "article.deleted, user.full_name, "
						+ "IF(article.created_date IS NULL,null, DATE_FORMAT(article.created_date, '%d-%m-%Y')) AS created_date FROM article "
						+ "LEFT JOIN article_type AS type ON article.type_id = type.article_type_id "
						+ "LEFT JOIN crm_user AS user ON article.created_by = user.user_id WHERE 1=1 ");
		builderGetTotal.append("SELECT COUNT(1) FROM article "
				+ "LEFT JOIN article_type AS type ON article.type_id = type.article_type_id "
				+ "LEFT JOIN crm_user AS user ON article.created_by = user.user_id ");
		// filter header
		if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
			builder.append(" AND article.deleted <> 1");
			builderGetTotal.append(" AND article.deleted <> 1");
		} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thùng rác
			builder.append(" AND article.deleted = 1");
			builderGetTotal.append(" AND article.deleted = 1");
		}
		if (Integer.parseInt(jsonParams.get("article_type").toString()) > -1) {
			builder.append(" AND article.type_id=" + jsonParams.get("article_type"));
			builderGetTotal.append(" AND article.type_id=" + jsonParams.get("article_type"));
		}
		if (jsonParams.get("article_title") != null && !"".equals(jsonParams.get("article_title").toString())) {
			builder.append(" AND article.article_title LIKE N'%" + jsonParams.get("article_title").toString()
					+ "%'");
			builderGetTotal.append(" AND article.article_title LIKE N'%"
					+ jsonParams.get("article_title").toString() + "%'");
		}
		// sortby
		if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
			switch (jsonParams.get("sortField").toString()) {
			default:
				builder.append(" ORDER BY article.created_date DESC");
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
}
