package com.we_learn.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class WrittingTestTopicDaoImp implements WrittingTestTopicDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(QADaoImpl.class);

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
		String wt_id = jsonParams.get("wt_id").toString();
		String content = jsonParams.get("wtt_content").toString();
		String query = "INSERT INTO `writing_test_topic`(`wt_id`, `wtt_content`, `is_premium`, `created_by`) "
				+ "VALUES (?, ?, IF((SELECT `group_id` FROM `crm_user` WHERE `user_id` = ?) <> 3, 0,1), ?)";
		try {
			Object[] objects = new Object[] {wt_id, content, user_id, user_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		return result;
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

		builder.append("SELECT wtt.`wtt_id`,wtt.`wtt_content`, wtt.`is_premium`, wtt.`created_date`, crm_user.full_name, "
				+ "(SELECT COUNT(wtc.wtc_id) FROM writing_test_comment wtc WHERE wtc.wtt_id = wtt.wtt_id) AS comments "
				+ "FROM `writing_test_topic` wtt LEFT JOIN crm_user ON (wtt.created_by = crm_user.user_id) WHERE 1 = 1");
		builderGetTotal.append("SELECT COUNT(1) FROM `writing_test_topic` wtt "
				+ "LEFT JOIN crm_user ON (crm_user.user_id = wtt.created_by) WHERE 1 = 1");
		// filter header
		if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
			builder.append(" AND wtt.deleted <> 1");
			builderGetTotal.append(" AND wtt.deleted <> 1");
		} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thùng rác
			builder.append(" AND wtt.deleted = 1");
			builderGetTotal.append(" AND deleted = 1");
		}
//		if (Integer.parseInt(jsonParams.get("type_id").toString()) > -1) {
//			builder.append(" AND article.type_id=" + jsonParams.get("type_id"));
//			builderGetTotal.append(" AND article.type_id=" + jsonParams.get("type_id"));
//		}
		if (jsonParams.get("full_name") != null && !"".equals(jsonParams.get("full_name").toString())) {
			builder.append(" AND crm_user.full_name LIKE N'%" + jsonParams.get("full_name").toString() + "%'");
			builderGetTotal
					.append(" AND crm_user.full_name LIKE N'%" + jsonParams.get("full_name").toString() + "%'");
		}
		// sortby
		if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
			switch (jsonParams.get("sortField").toString()) {
			default:
				builder.append(" ORDER BY wtt.created_date DESC");
				break;
			}
//			if (jsonParams.get("sortOrder") != null && "descend".equals(jsonParams.get("sortOrder").toString())) {
//				builder.append(" DESC");
//			}
//			if (jsonParams.get("sortOrder") != null && "ascend".equals(jsonParams.get("sortOrder").toString())) {
//				builder.append(" ASC");
//			}
		}
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
			data.put("msg", "Lấy danh mục thất bại");
		}
		return data;
	}

	@Override
	public JSONObject getById(String wtt_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		String query = "SELECT wtc.wtc_content, wtc.is_admin, crm_user.full_name,"
				+ "DATE_FORMAT(wtc.created_date, '%d-%m-%Y %h:%i') AS created_date  "
				+ "FROM `writing_test_topic` wtt LEFT JOIN writing_test_comment wtc ON (wtc.wtt_id = wtt.wtt_id) "
				+ "LEFT JOIN crm_user ON (crm_user.user_id = wtc.created_by) WHERE wtt.wtt_id = ?";
		try {
//			Map<String, Object> test = this.jdbcTemplate.queryForMap(queryForWrittingTest, new Object[] {wt_id});
			List<Map<String, Object>> lstComment = this.jdbcTemplate.queryForList(query, new Object[] {wtt_id});
//			test.put("lstTopic", lstTopic);
			result.put("data", lstComment);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "Lấy danh sách bài kiểm tra thất bại");
		}
		return result;
	}
}
