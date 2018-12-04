package com.we_learn.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class TestDaoImp implements TestDao{
	
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
		String name = jsonParams.get("name").toString();
		String type = jsonParams.get("type").toString();
		String query = "INSERT INTO `test`(`test_name`, `test_type`, `created_by`) VALUES (?,?,?)";
		try {
			Object[] objects = new Object[] {name, type, user_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Test create success");
		return result;
	}

	@Override
	public JSONObject update(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String name = jsonParams.get("name").toString();
		String type = jsonParams.get("type").toString();
		String test_id = jsonParams.get("test_id").toString();
		String query = "UPDATE `test` SET `test_name`=?,`test_type`=?,`modify_date`=?,`modify_by`=? WHERE `test_id` = ?";
		//insert
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			Object[] objects = new Object[] {name, type, dateTimeNow, user_id, test_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Test update success");
		return result;
	}

	@Override
	public JSONObject getById(String param) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
//		MainUtility mainUtil = new MainUtility();
//		JSONObject jsonParams = mainUtil.stringToJson(param);
//		String test_id = jsonParams.get("test_id").toString();
//		String query = "SELECT `test_id`, `test_name`, `test_type`, `created_date`, `created_by` FROM `test` WHERE `test_id` = ? AND `deleted` = 0";
//		try {
//			List<Map<String, Object>> l = this.jdbcTemplate.queryForList(query);
//			JSONObject results = new JSONObject();
//			results.put("results", listQA);
//			results.put("total", totalRow);
//			result.put("data", results);
//			result.put("success", true);
//		} catch (Exception e) {
//			result.put("success", false);
//			result.put("err", e.getMessage());
//			result.put("msg", "Lấy danh sách bài viết thất bại");
//		}
		return result;
	}

	@Override
	public JSONObject getAll() {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		String query = "SELECT `test_id`, `test_name`, `test_type`,DATE_FORMAT(`created_date`, '%d-%m-%Y') AS `created_date`, `created_by` FROM `test` WHERE `deleted` = 0";
		try {
			List<Map<String, Object>> listTest = this.jdbcTemplate.queryForList(query);
			JSONObject results = new JSONObject();
			results.put("results", listTest);
			result.put("data", results);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "Lấy danh sách bài kiểm tra thất bại");
		}
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