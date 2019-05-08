package com.we_learn.domain;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.we_learn.common.VerifyToken;
import com.we_learn.dao.CourseCommentDao;
import com.we_learn.dao.CourseCommentDaoImpl;
import com.we_learn.dao.QACommentDao;
import com.we_learn.dao.QACommentDaoImp;

@Path("/course-comment")
public class CourseCommentController extends VerifyToken{
	public CourseCommentController(@HeaderParam("Authorization") String token) {
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
		CourseCommentDao courseCommentDao = (CourseCommentDaoImpl) this.appContext.getBean("courseCommentDao");
		JSONObject result = courseCommentDao.insert(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@HeaderParam("Authorization") String token, String param) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseCommentDao courseCommentDao = (CourseCommentDaoImpl) this.appContext.getBean("courseCommentDao");
		JSONObject result = courseCommentDao.update(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("list-comment-by-page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response listCommentByPage(@HeaderParam("Authorization") String token, String param) {
//		if (!this.isLogined)
//			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseCommentDao courseCommentDao = (CourseCommentDaoImpl) this.appContext.getBean("courseCommentDao");
		JSONObject result = courseCommentDao.listCommentByPage(param);
		return Response.status(200).entity(result.toString()).build();
	}
}
