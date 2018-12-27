package com.we_learn.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class WrittingTestDaoImp implements WrittingTestDao{
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
		String title = jsonParams.get("wt_title").toString();
		String content = jsonParams.get("wt_content").toString();
		String query = "INSERT INTO `writing_test`(`wt_title`, `wt_content`, `created_by`) VALUES (?,?,?)";
		try {
			Object[] objects = new Object[] {title, content, user_id};
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
	public JSONObject getById(String wt_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		String queryForWrittingTest = "SELECT `wt_title`, `wt_content` FROM `writing_test` WHERE `wt_id` = ?";
		String queryForTopic = "SELECT wtt.wtt_content FROM `writing_test` wt LEFT JOIN writing_test_topic wtt ON (wt.wt_id = wtt.wt_id) WHERE wt.wt_id = ?";
		try {
			Map<String, Object> test = this.jdbcTemplate.queryForMap(queryForWrittingTest, new Object[] {wt_id});
			List<Map<String, Object>> lstTopic = this.jdbcTemplate.queryForList(queryForTopic, new Object[] {wt_id});
			test.put("lstTopic", lstTopic);
			result.put("data", test);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "Lấy danh sách bài kiểm tra thất bại");
		}
		return result;
	}

	@Override
	public JSONObject getByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		StringBuilder builder = new StringBuilder();
		StringBuilder builderGetTotal = new StringBuilder();

		builder.append("SELECT `wt_id`, `wt_title`, `wt_content` FROM `writing_test` WHERE 1=1");
		builderGetTotal.append("SELECT COUNT(1) FROM `writing_test` "
				+ " WHERE 1=1");
		// filter header
		if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
			builder.append(" AND deleted <> 1");
			builderGetTotal.append(" AND deleted <> 1");
		} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thùng rác
			builder.append(" AND deleted = 1");
			builderGetTotal.append(" AND deleted = 1");
		}
		if (jsonParams.get("wt_title") != null && !"".equals(jsonParams.get("wt_title").toString())) {
			builder.append(" AND wt_title LIKE N'%" + jsonParams.get("wt_title").toString() + "%'");
			builderGetTotal
					.append(" AND wt_title LIKE N'%" + jsonParams.get("wt_title").toString() + "%'");
		}
		if (jsonParams.get("wt_content") != null && !"".equals(jsonParams.get("wt_content").toString())) {
			builder.append(" AND wt_content LIKE N'%" + jsonParams.get("wt_content").toString() + "%'");
			builderGetTotal
					.append(" AND wt_content LIKE N'%" + jsonParams.get("wt_content").toString() + "%'");
		}
		// sortby
		if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
			switch (jsonParams.get("sortField").toString()) {
			default:
				builder.append(" ORDER BY created_date DESC");
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
			List<Map<String, Object>> lstWrittingTest = this.jdbcTemplate.queryForList(builder.toString());
			JSONObject results = new JSONObject();
			results.put("results", lstWrittingTest);
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
}
