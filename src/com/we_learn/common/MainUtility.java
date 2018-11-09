package com.we_learn.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.jdbc.core.JdbcTemplate;

public class MainUtility {
	public JSONObject stringToJson(String params) {
		JSONObject result = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			result = (JSONObject) parser.parse(params);
		} catch (ParseException e) {
			return null;
		}
		return result;
	}
	
	public String getDateFormat(String format, Date date) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.format(date);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	
	public int getPageIndex(String page) {
		int pageIndex = Integer.parseInt(page);
		int index = (pageIndex - 1) * 10;
		return index;
	}
	
	public JSONObject notFoundObject() {
		JSONObject result = new JSONObject();
		result.put("success", false);
		result.put("code", "404");
		result.put("error", "Bản ghi không tồn tại");
		return result;
	}
	
	public boolean checkPermission(String group_code, JdbcTemplate jdbcTemplate, String permissionStr) {
		boolean result = false;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT EXISTS (SELECT 1 FROM crm_group_permission AS gp INNER JOIN crm_permission AS p "
				+ "ON gp.permission_id = p.permission_id INNER JOIN crm_group AS g ON gp.group_id = g.group_id "
				+ "WHERE g.group_code = ? ");
		sql.append("AND p.permission_code = ? )");

		if (jdbcTemplate.queryForObject(sql.toString(), new Object[] { group_code, permissionStr },
				Integer.class) == 1) {
			result = true;
		}
		return result;
	}
	
	public JSONObject noPermissionObject(boolean redirect) {
		JSONObject result = new JSONObject();
		result.put("success", false);
		if (redirect)
			result.put("code", 404);
		result.put("msg", "Bạn không có quyền thao tác");
		return result;
	}
}
