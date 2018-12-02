package com.we_learn.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.we_learn.dao.TestDaoImpl;
import com.we_learn.common.MainUtility;

public class TestDaoImpl implements TestDao {
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(TestDaoImpl.class);

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
		// TransactionDefinition txDef = new DefaultTransactionDefinition();
		// TransactionStatus txStatus = this.transactionManager.getTransaction(txDef);
		// insert

		String sqlInsertTest = "INSERT INTO test (test_name, test_type, create_date, create_by, deleted)"
				+ " VALUES (?,?,?,?,?)";
		String sqlInsertTestQuestion = "INSERT INTO test_question (test_id, tq_content,"
				+ " create_date, create_by, deleted)" + " VALUES (?,?,?,?,?)";
		String sqlInsertTestAnswer = "INSERT INTO test_answer (tq_id, ta_content,"
				+ " create_date, create_by, deleted)" + " VALUES (?,?,?,?,?)";
		try {

			// Object[] objects = new Object[] {title, content, user_id, type};
			// int row = this.jdbcTemplate.update(query, objects);
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
	public JSONObject getTestByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		StringBuilder builder = new StringBuilder();
		StringBuilder builderGetTotal = new StringBuilder();

		builder.append("SELECT article.article_id, article.article_title, type.article_type_name, "
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
			builder.append(" AND article.article_title LIKE N'%" + jsonParams.get("article_title").toString() + "%'");
			builderGetTotal
					.append(" AND article.article_title LIKE N'%" + jsonParams.get("article_title").toString() + "%'");
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

	@Override
	public JSONObject getTestById(String article_id) {
		JSONObject result = new JSONObject();
		String query = "SELECT article.type_id AS article_type, article.article_title, article.article_content "
				+ "FROM article " + "WHERE article.article_id = " + article_id;
		String queryForComments = "SELECT comment_id,DATE_FORMAT(created_date, '%H:%i %d-%m-%Y') AS `created_date`, content, crm_user.full_name FROM `article_comment` LEFT JOIN crm_user ON crm_user.user_id = article_comment.user_id WHERE article_id = ? ORDER BY created_date DESC";
		try {
			Map<String, Object> articleObject = this.jdbcTemplate.queryForMap(query);
			// List<Map<String, Object>> listComments =
			// this.jdbcTemplate.queryForList(queryForComments, new Object[] {article_id});
			result.put("success", true);
			result.put("data", articleObject);
			// result.put("comments", listComments);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
			// result.put("msg", "Không tồn tại loại địa điểm");
		}
		return result;
	}
}
