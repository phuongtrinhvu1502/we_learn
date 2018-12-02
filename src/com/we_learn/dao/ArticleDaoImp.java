package com.we_learn.dao;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.we_learn.common.MainUtility;

public class ArticleDaoImp implements ArticleDao{
	private JdbcTemplate jdbcTemplate;
	private Logger logger = Logger.getLogger(CreateTestDaoImpl.class);

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public JSONObject insert(String param, String user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject update(String param, String user_id) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public JSONObject getListArticleByType(String param) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		MainUtility mainUtil = new MainUtility();
		JSONObject jsonParams = mainUtil.stringToJson(param);
		String article_type_id = jsonParams.get("article_type_id").toString();
		String queryForListArticle = "SELECT `article_id`, `article_title`, `article_content` FROM `article` WHERE `type_id` = ? AND `deleted` <> 1";
		try {
			List<Map<String, Object>> listArticle = this.jdbcTemplate.queryForList(queryForListArticle, new Object[] {article_type_id});
			for (Map<String, Object> article : listArticle) {
				String queryForListTopic = "SELECT `at_id`, `at_title`, `at_content` FROM `article_topic` WHERE `article_id` = ? AND `deleted` <> 1";
				List<Map<String, Object>> listTopic = this.jdbcTemplate.queryForList(queryForListTopic, new Object[] {article.get("article_id")});
				for (Map<String, Object> topic : listTopic) {
					String queryForListContent = "SELECT `atc_id`, `atc_title`, `atc_content` FROM `article_topic_content` WHERE `at_id` = ? AND `deleted` <> 1";
					List<Map<String, Object>> listContent = this.jdbcTemplate.queryForList(queryForListContent, new Object[] {topic.get("at_id")});
					topic.put("listContent", listContent);
				}
				article.put("listTopic", listTopic);
			}
			result.put("success", true);
			result.put("data", listArticle);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
			return result;
		}
		
	}
}
