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
import com.we_learn.dao.TestDao;
import com.we_learn.dao.TestDaoImp;

@Path("/test")
public class Test extends VerifyToken{
	public Test(@HeaderParam("Authorization") String token) {
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
		TestDao testDao = (TestDaoImp) this.appContext.getBean("testDao");
		JSONObject result = testDao.insert(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@HeaderParam("Authorization") String token, String param) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		TestDao testDao = (TestDaoImp) this.appContext.getBean("testDao");
		JSONObject result = testDao.update(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("get-list-test")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListTest(@HeaderParam("Authorization") String token, String param) {
		
		TestDao testDao = (TestDaoImp) this.appContext.getBean("testDao");
		JSONObject result = testDao.getAll();
		return Response.status(200).entity(result.toString()).build();
	}
}
