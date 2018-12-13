package com.we_learn.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class WritingTestDaoImp implements WritingTestDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(UserDaoImpl.class);

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
		String content = jsonParams.get("content").toString();
		String query = "INSERT INTO `writing_test`(`wt_title`, `wt_content`,`created_by`) VALUES (?,?,?)";
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
	public JSONObject update(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String title = jsonParams.get("title").toString();
		String content = jsonParams.get("content").toString();
		String wt_id = jsonParams.get("wt_id").toString();
		String query = "UPDATE `writing_test` SET `wt_title`=?,`wt_content`=?,`mofidy_date`=?,`modify_by`=? WHERE `wt_id` = ?";
		try {
			String dateTimeNow = mainUtil.getDateFormat("yyyy-MM-dd HH:mm:ss", new Date());
			Object[] objects = new Object[] {title, content, dateTimeNow, user_id, wt_id};
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
	public JSONObject delete(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String query = "DELETE FROM writing_test WHERE wt_id IN ("
				+ jsonParams.get("wt_id") + ")";
		try {
			int row = this.jdbcTemplate.update(query);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			// result.put("msg", "Xóa b);
			result.put("msg", "Xóa bài viết thất bại");
		}
		return result;
	}

	@Override
	public JSONObject remove(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		// Sẽ phải check bên place địa điểm đã sử dụng ở bản ghi nào chưa
		try {
			String query = "UPDATE `writing_test` SET `mofidy_date`=?,`modify_by`=?,`deleted`=1 WHERE `wt_id` = ?";
			int row = this.jdbcTemplate.update(query,
					new Object[] { mainUtil.dateToStringFormat(new Date(), "yyyy-MM-dd HH:mm:ss"), user_id,
							jsonParams.get("wt_id") });
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
			// result.put("msg", "Chuyển loại địa điểm vào thùng rác thất bại");
		}
		return result;
	}

	@Override
	public JSONObject getById(String param) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String wt_id = jsonParams.get("wt_id").toString();
		String queryForContent = "SELECT `wt_content` FROM `writing_test` WHERE `wt_id` = ?";
		String queryForTeacherComment = "SELECT writing_test_comment.`wtc_id`, writing_test_comment.`wtc_content`, "
				+ "writing_test_comment.`created_date`, user.user_full_name FROM `writing_test_comment` "
				+ "LEFT JOIN crm_user user ON (writing_test_comment.created_by = user.user_id) "
				+ "WHERE `wt_id` = ? AND `is_admin` = 1 AND `deleted` = 0";
		String queryForUserComment = "SELECT writing_test_comment.`wtc_id`, writing_test_comment.`wtc_content`, "
				+ "writing_test_comment.`created_date`, user.user_full_name FROM `writing_test_comment` "
				+ "LEFT JOIN crm_user user ON (writing_test_comment.created_by = user.user_id) "
				+ "WHERE `wt_id` = ? AND `is_admin` = 0 AND `deleted` = 0";
		try {
			String content = this.jdbcTemplate.queryForObject(queryForContent, String.class, new Object[] {wt_id});
			List<Map<String, Object>> lstTeacherComment = this.jdbcTemplate.queryForList(queryForTeacherComment, new Object[] {wt_id});
			List<Map<String, Object>> lstUserComment = this.jdbcTemplate.queryForList(queryForUserComment, new Object[] {wt_id});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@Override
	public JSONObject getListByPage(String param) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		StringBuilder builder = new StringBuilder();
		StringBuilder builderGetTotal = new StringBuilder();

		builder.append("SELECT wt.`wt_id`, wt.`wt_title`, wt.`created_date`, "
				+ "u.full_name FROM `writing_test` wt LEFT JOIN crm_user u ON(wt.`created_by` = u.user_id) WHERE 1 = 1");
		builderGetTotal.append("SELECT COUNT(1) FROM writing_test "
				+ "WHERE 1=1");
		// filter header
		if (jsonParams.get("status") == null || Integer.parseInt(jsonParams.get("status").toString()) == -1) {
			builder.append(" AND wt.deleted <> 1");
			builderGetTotal.append(" AND deleted <> 1");
		} else if (Integer.parseInt(jsonParams.get("status").toString()) == -2) {// thùng rác
			builder.append(" AND wt.deleted = 1");
			builderGetTotal.append(" AND deleted = 1");
		}
//		if (Integer.parseInt(jsonParams.get("type_id").toString()) > -1) {
//			builder.append(" AND article.type_id=" + jsonParams.get("type_id"));
//			builderGetTotal.append(" AND article.type_id=" + jsonParams.get("type_id"));
//		}
		if (jsonParams.get("wt_title") != null && !"".equals(jsonParams.get("wt_title").toString())) {
			builder.append(" AND wt.wt_title LIKE N'%" + jsonParams.get("wt_title").toString() + "%'");
			builderGetTotal
					.append(" AND wt_title LIKE N'%" + jsonParams.get("wt_title").toString() + "%'");
		}
		// sortby
		if (jsonParams.get("sortField") != null && !"".equals(jsonParams.get("sortField").toString())) {
			switch (jsonParams.get("sortField").toString()) {
			default:
				builder.append(" ORDER BY wt.created_date DESC");
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
}
