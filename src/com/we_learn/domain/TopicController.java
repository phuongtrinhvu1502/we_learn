package com.we_learn.domain;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.we_learn.common.MainUtility;
import com.we_learn.common.VerifyToken;
import com.we_learn.dao.QADao;
import com.we_learn.dao.QADaoImpl;
import com.we_learn.dao.TopicDao;
import com.we_learn.dao.TopicDaoImp;

@Path("/article")
public class TopicController extends VerifyToken{
	public TopicController(@HeaderParam("Authorization") String token) {
		super(token);
		// TODO Auto-generated constructor stub
	}
	@Context
	private ServletContext context;
	// private WebApplicationContext appContext = null;
	@Autowired
	private WebApplicationContext appContext = ContextLoader.getCurrentWebApplicationContext();
	@POST
	@Path("insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insert(@HeaderParam("Authorization") String token, String param) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.insert(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@HeaderParam("Authorization") String token, String param) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.update(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@PUT
	@Path("remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@HeaderParam("Authorization") String token, String article) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.remove(article, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}

	@PUT
	@Path("restore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response restore(@HeaderParam("Authorization") String token, String article) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.restore(article, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@HeaderParam("Authorization") String token, String article) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.delete(article, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@POST
	@Path("get-article-by-page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTopicByPage(@HeaderParam("Authorization") String token, String param) {
//		if (!this.isLogined)
//			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.getTopicByPage(param);
		return Response.status(200).entity(result.toString()).build();
	}
	
	@GET
	@Path("get-article-by-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticleById(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.getArticleById(request.getParameter("article_id"));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@GET
	@Path("view-article-by-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewArticleById(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TopicDao topicDao = (TopicDaoImp) this.appContext.getBean("topicDao");
		JSONObject result = topicDao.viewArticleById(request.getParameter("article_id"));
		return Response.status(200).entity(result.toString()).build();
	}
}
