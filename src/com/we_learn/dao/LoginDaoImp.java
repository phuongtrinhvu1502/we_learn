package com.we_learn.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.we_learn.common.MainUtility;

import redis.clients.jedis.Jedis;





public class LoginDaoImp implements LoginDao{
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject login(String params) {
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(params);
		String password = jsonParams.get("password").toString();
		String passwordMd5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			passwordMd5 = DatatypeConverter.printHexBinary(digest).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
		}

		String query = "SELECT crm_user.*, crm_group.group_code FROM crm_user LEFT JOIN crm_group ON crm_user.group_id = crm_group.group_id"
				+ " WHERE user_login = ? AND password = ?";
		try {
			Map<String, Object> user = this.jdbcTemplate.queryForMap(query,
					new Object[] { jsonParams.get("username").toString(), passwordMd5 });
			Date date = new Date();
			long currentTimeMillis = System.currentTimeMillis();
			Date expireDate = new Date(currentTimeMillis + (24 * 60 * 60 * 10 * 1000));
			Algorithm algorithm = Algorithm.HMAC256("biPHxAMbw7H0mUfV3xO1TIpv0nAQfK41");

			String token = JWT.create().withClaim("id", user.get("user_id").toString())
					.withClaim("username", user.get("user_login").toString())
					.withClaim("group_code", user.get("group_code").toString()).withIssuedAt(date)
					.withExpiresAt(expireDate).sign(algorithm);

			Jedis jedis = new Jedis("localhost");
			jedis.get(token);
			String queryGetAllPermission = "SELECT p.permission_code FROM crm_group_permission AS gp "
					+ "INNER JOIN crm_permission AS p ON gp.permission_id = p.permission_id "
					+ "INNER JOIN crm_group ON gp.group_id = crm_group.group_id WHERE gp.deleted <> 1 AND crm_group.group_code = ?";
			List<Map<String, Object>> lstPermission = this.jdbcTemplate.queryForList(queryGetAllPermission,
					new Object[] { user.get("group_code").toString() });
			List<String> arrPermission = new ArrayList<>();
			for (Map<String, Object> i : lstPermission) {
				arrPermission.add(i.get("permission_code").toString());
			}

			JSONObject userJson = new JSONObject();
			userJson.put("id", user.get("user_id"));
			userJson.put("username", user.get("user_login"));
			userJson.put("group_code", user.get("group_code"));
			// userJson.put("lst_permission", arrPermission);
			jedis.set(token, userJson.toString());
			jedis.close();
		
			userJson.put("username", user.get("full_name"));
			result.put("success", true);
			result.put("data", userJson);
			result.put("lstPermission", arrPermission);
			result.put("Authorization", "Bearer " + token);
		} catch (EmptyResultDataAccessException e) {
			result.put("success", false);
			result.put("msg", "Login fail.");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}

	@Override
	public JSONObject logout(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
