package com.we_learn.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.we_learn.common.MainUtility;

public class UserAnswerDaoImp implements UserAnswerDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(QADaoImpl.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject userAnswer(String param, String user_id) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		JSONArray user_answer = (JSONArray) jsonParams.get("user_answer");
		String test_id = jsonParams.get("test_id").toString();
		String queryForCorrectAnswer = "SELECT `ta_id` FROM `correct_answer` WHERE `tq_id` = ?";
		int correctTime = 0;
		try {
			for (int i = 0; i < user_answer.size(); i++) {
				JSONObject answer = (JSONObject) user_answer.get(i);
				int ta_id = this.jdbcTemplate.queryForObject(queryForCorrectAnswer, Integer.class, new Object[] {answer.get("tq_id").toString()});
				int us_choice = (Integer) answer.get("us_choice");
				if (us_choice == ta_id) {
					correctTime++;
				}
			}
			
			//insert user result
			String queryForInsertUserResult = "INSERT INTO `user_result`(`test_id`, `result`, `created_by`) VALUES (?,?,?)";
			double testResult = correctTime/user_answer.size();
			int row = this.jdbcTemplate.update(queryForInsertUserResult, new Object[] {test_id, String.valueOf(testResult), user_id});
			//insert user_answer
			String sqlForInsertUserAnswer = "INSERT INTO `user_answer`(`test_id`, `tq_id`, `us_choice`, `created_by`) VALUES (?,?,?,?)";
			this.jdbcTemplate.batchUpdate(sqlForInsertUserAnswer, new BatchPreparedStatementSetter() {
				
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					// TODO Auto-generated method stub
					JSONObject answer = (JSONObject) user_answer.get(i);
					ps.setString(1, test_id);
					ps.setString(2, answer.get("tq_id").toString());
					ps.setString(3, answer.get("us_choice").toString());
					ps.setString(4, user_id);
				}
				
				@Override
				public int getBatchSize() {
					// TODO Auto-generated method stub
					return user_answer.size();
				}
			});
			result.put("success", true);
			result.put("correct_anwser", correctTime);
			result.put("total", user_answer.size());
		} catch (Exception e) {
			// TODO: handle exception
			result.put("success", false);
			result.put("err", e.getMessage());
			result.put("msg", "Kiểm tra kết quả thất bại");
		}
		return result;
	}
}
