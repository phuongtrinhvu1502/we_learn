package com.we_learn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.we_learn.common.MainUtility;

public class DocumentDaoImpl implements DocumentDao {
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(QADaoImpl.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject insert(Map<String, Object> jsonParams, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		String sqlInsertDoc = "INSERT INTO `wl_document`(`file_name`,`file_path`, create_date, `create_by`) VALUES (?,?,?,?)";
		// insert
		try {
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sqlInsertDoc, Statement.RETURN_GENERATED_KEYS);
					int count = 1;
					mainUtil.setParam(ps, jsonParams.get("file_name"), "string", count++);
					mainUtil.setParam(ps, jsonParams.get("file_path"), "string", count++);
					mainUtil.setParam(ps, mainUtil.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), "string",
							count++);
					mainUtil.setParam(ps, user_id, "int", count++);
					return ps;
				}
			}, holder);
			result.put("success", true);
			result.put("doc_id", holder.getKey().intValue());
			result.put("msg", "Upload successfully");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		return result;
	}

	@Override
	public JSONObject getAllDocument(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String qa_id = jsonParams.get("qa_id").toString();
		StringBuilder queryForComment = new StringBuilder();
		queryForComment
				.append("SELECT t1.`qa_comment_id` AS `comment_id`, t1.`qa_comment_content` AS `comment_content`,"
						+ "IF(t1.`created_date` IS NULL,null, DATE_FORMAT(t1.`created_date`, '%d-%m-%Y %H:%i:%s')) AS `created_date`, "
						+ "t2.`user_id`, t2.`user_login` FROM `qa_comment` t1 LEFT JOIN `crm_user` t2 ON t2.`user_id` = t1.`created_by` "
						+ "WHERE `qa_id` = ? AND t2.`deleted` <> 1 ORDER BY t1.`created_date` DESC");
		String queryForTotal = "SELECT COUNT(1) FROM `qa_comment` WHERE `qa_id` = ?";
		mainUtil.getLimitOffset(queryForComment, jsonParams);
		try {
			int totalRow = this.jdbcTemplate.queryForObject(queryForTotal, Integer.class, new Object[] { qa_id });
			List<Map<String, Object>> listComment = this.jdbcTemplate.queryForList(queryForComment.toString(),
					new Object[] { qa_id });
			JSONObject results = new JSONObject();
			results.put("results", listComment);
			results.put("total", totalRow);
			data.put("data", results);
			data.put("success", true);
		} catch (Exception e) {
			data.put("success", false);
			data.put("err", e.getMessage());
			data.put("msg", "Lấy danh sách comment thất bại");
		}
		return data;
	}

}
