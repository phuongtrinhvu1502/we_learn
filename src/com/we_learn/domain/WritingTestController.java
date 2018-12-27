package com.we_learn.domain;

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

import com.we_learn.common.VerifyToken;
import com.we_learn.dao.QADao;
import com.we_learn.dao.QADaoImpl;
import com.we_learn.dao.WrittingTestDao;

@Path("/writing-test")
public class WritingTestController extends VerifyToken{
	public WritingTestController(@HeaderParam("Authorization") String token) {
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
		WrittingTestDao writtingTestDao = (WrittingTestDao) this.appContext.getBean("writingTestDao");
		JSONObject result = writtingTestDao.insert(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	
	
	@POST
	@Path("get-wt-by-page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWtByPage(@HeaderParam("Authorization") String token, String param) {
//		if (!this.isLogined)
//			return Response.status(200).entity(this.notFoundUser().toString()).build();
		WrittingTestDao writtingTestDao = (WrittingTestDao) this.appContext.getBean("writingTestDao");
		JSONObject result = writtingTestDao.getByPage(param);
		return Response.status(200).entity(result.toString()).build();
	}
	
	@GET
	@Path("get-wt-by-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWtById(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		WrittingTestDao writtingTestDao = (WrittingTestDao) this.appContext.getBean("writingTestDao");
		JSONObject result = writtingTestDao.getById(request.getParameter("wt_id"));
		return Response.status(200).entity(result.toString()).build();
	}
	@GET
	@Path("get-wt-by-user-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWtByUserId(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		WrittingTestDao writtingTestDao = (WrittingTestDao) this.appContext.getBean("writingTestDao");
		JSONObject result = writtingTestDao.getAllByUserId(request.getParameter("wt_id"), this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
}
