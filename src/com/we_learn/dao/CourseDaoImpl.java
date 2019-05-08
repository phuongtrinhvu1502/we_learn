package com.we_learn.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class CourseDaoImpl implements CourseDao{
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
				String title = jsonParams.get("course_title").toString();
				String content = jsonParams.get("course_content").toString();
				
				String query = "INSERT INTO `course`(`course_title`, `course_content`, `created_by`) VALUE (?,?,?)";
				//insert
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
				result.put("msg", "Course create success");
				return result;
	}

	@Override
	public JSONObject update(String param, String user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("course_title").toString();
		String content = jsonParams.get("course_content").toString();
		String qa_id = jsonParams.get("course_id").toString();
		String query = "UPDATE `course` SET `course_title`=?, `course_content` =?, `modify_date` =?, `modify_by` = ? WHERE `course_id` = ?";
		//insert
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			Object[] objects = new Object[] {title, content, dateTimeNow, user_id, qa_id};
			int row = this.jdbcTemplate.update(query, objects);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		result.put("success", true);
		result.put("msg", "Course update success");
		return result;
	}

	@Override
	public JSONObject delete(String qa, int user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(qa);
		String query = "DELETE FROM course WHERE course_id IN ("
				+ jsonParams.get("course_id") + ")";
		try {
			int row = this.jdbcTemplate.update(query);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
//			result.put("msg", "XÃ³a b);
			 result.put("msg", "XÃ³a khÃ³a há»�c tháº¥t báº¡i");
		}
		return result;
	}

	@Override
	public JSONObject remove(String qa, int user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(qa);
		// Sáº½ pháº£i check bÃªn place Ä‘á»‹a Ä‘iá»ƒm Ä‘Ã£ sá»­ dá»¥ng á»Ÿ báº£n ghi nÃ o chÆ°a
		try {
			String query = "UPDATE course AS qa SET qa.deleted = 1, qa.modify_date = ?, "
					+ "qa.modify_by = ? WHERE qa.course_id = ?";
			int row = this.jdbcTemplate.update(query,
					new Object[] { mainUtil.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), user_id,
							jsonParams.get("course_id") });
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@Override
	public JSONObject restore(String qa, int user_id) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(qa);
		String sql = "UPDATE course AS qa SET qa.deleted = 0, qa.modify_date = ?, qa.modify_by = ?"
				+ " WHERE qa.course_id IN (" + jsonParams.get("course_id") + ")";
		try {
			this.jdbcTemplate.update(sql,
					new Object[] { mainUtil.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), user_id });
			result.put("success", true);
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.put("success", false);
			result.put("msg", e.getMessage());
			// result.put("msg", "Restore loáº¡i Ä‘á»‹a Ä‘iá»ƒm tháº¥t báº¡i");
		}
		return result;
	}

	@Override
	public JSONObject getCourseById(String qa_id) {
		JSONObject result = new JSONObject();
		String query = "SELECT qa.course_title, qa.course_content "
				+ "FROM course AS qa "
				+ "WHERE qa.course_id = " + qa_id;
		try {
			Map<String, Object> qaObject = this.jdbcTemplate.queryForMap(query);

			result.put("success", true);
			result.put("data", qaObject);
		} catch (Exception e) {
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "KhÃ´ng láº¥y Ä‘Æ°á»£c thÃ´ng tin khÃ³a há»�c. Kiá»ƒm tra láº¡i");
		}
		return result;
	}

	@Override
	public JSONObject viewCourseById(String qa_id) {
		JSONObject result = new JSONObject();
		String query = "SELECT qa.course_title, qa.course_content, qa.course_url, user.user_login "
				+ "FROM course AS qa "
				+ "LEFT JOIN crm_user AS user ON qa.created_by = user.user_id "
				+ "WHERE qa.course_id = " + qa_id;
		try {
			Map<String, Object> qaObject = this.jdbcTemplate.queryForMap(query);

			result.put("success", true);
			result.put("data", qaObject);
		} catch (Exception e) {
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "KhÃ´ng láº¥y Ä‘Æ°á»£c thÃ´ng tin khÃ³a há»�c. Kiá»ƒm tra láº¡i");
		}
		return result;
	}

	@Override
	public JSONObject getCourseByPage(String param) {
		// TODO Auto-generated method stub
				JSONObject data = new JSONObject();
				JSONObject result = new JSONObject();
				MainUtility mainUtil = new MainUtility();
				JSONObject jsonParams = mainUtil.stringToJson(param);
				StringBuilder builder = new StringBuilder();
				StringBuilder builderGetTotal = new StringBuilder();
				
				builder.append(
						"SELECT qa.course_id, qa.course_title, (SELECT COUNT(course_id) FROM course_comment AS qa_comment WHERE qa_comment.course_id = qa.course_id) AS comment_number, "
								+ "qa.deleted, user.full_name, "
								+ "IF(qa.created_date IS NULL,null, DATE_FORMAT(qa.created_date, '%d-%m-%Y')) AS created_date FROM course AS qa "
								+ "LEFT JOIN crm_user AS user ON qa.created_by = user.user_id WHERE 1=1 ");
				builderGetTotal.append("SELECT COUNT(1) FROM course AS qa "
						+ "LEFT JOIN crm_user AS user ON qa.created_by = user.user_id ");
				// filter header
				if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
					builder.append(" AND qa.deleted <> 1");
					builderGetTotal.append(" AND qa.deleted <> 1");
				} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thÃ¹ng rÃ¡c
					builder.append(" AND qa.deleted = 1");
					builderGetTotal.append(" AND qa.deleted = 1");
				}
				if (jsonParams.get("course_title") != null && !"".equals(jsonParams.get("course_title").toString())) {
					builder.append(" AND qa.course_title LIKE N'%" + jsonParams.get("course_title").toString()
							+ "%'");
					builderGetTotal.append(" AND qa.course_title LIKE N'%"
							+ jsonParams.get("course_title").toString() + "%'");
				}
				// sortby
				if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
					switch (jsonParams.get("sortField").toString()) {
					default:
						builder.append(" ORDER BY qa.created_date DESC");
						break;
					}
					// sortOrder chá»‰ lÃ  descend vÃ  ascend hoáº·c rá»—ng
					if (jsonParams.get("sortOrder") != null && "descend".equals(jsonParams.get("sortOrder").toString())) {
						builder.append(" DESC");
					}
					if (jsonParams.get("sortOrder") != null && "ascend".equals(jsonParams.get("sortOrder").toString())) {
						builder.append(" ASC");
					}
				}
				// láº¥y cÃ¡c biáº¿n tá»« table (limit, offset)
				mainUtil.getLimitOffset(builder, jsonParams);
				try {
					int totalRow = this.jdbcTemplate.queryForObject(builderGetTotal.toString(), Integer.class);
					List<Map<String, Object>> listQA = this.jdbcTemplate.queryForList(builder.toString());
					JSONObject results = new JSONObject();
					results.put("results", listQA);
					results.put("total", totalRow);
					data.put("data", results);
					data.put("success", true);
				} catch (Exception e) {
					e.printStackTrace();
					data.put("success", false);
					data.put("err", e.getMessage());
					data.put("msg", "Lấy danh sách khóa học thất bại");
				}
				return data;
	}

}
