package com.we_learn.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Properties;

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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.we_learn.common.MainUtility;
import com.we_learn.common.VerifyToken;
import com.we_learn.dao.CourseDao;
import com.we_learn.dao.CourseDaoImpl;
import com.we_learn.dao.QADao;
import com.we_learn.dao.QADaoImpl;

@Path("/course")
public class CourseController extends VerifyToken{
	public CourseController(@HeaderParam("Authorization") String token) {
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
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	public Response insert(@HeaderParam("Authorization") String token, 
			@FormDataParam("file") InputStream uploadedInputStream, 
    		@FormDataParam("file") FormDataContentDisposition fileDetail,
    		@FormDataParam("param") String param) throws Exception {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		String configDirRoot = context.getRealPath("/WEB-INF/classes/config.properties");
		File configFile = new File(configDirRoot);
		FileReader reader;
		Properties props = new Properties();
		reader = new FileReader(configFile);
		props.load(reader);
		String fileFolder = props.getProperty("path.urlFileWrite");
		MainUtility utility = new MainUtility();
		String location = utility.getUploadFileLocation(fileFolder, fileDetail.getFileName());
		JSONObject result = courseDao.insert(param, this.userId, uploadedInputStream, location);
		return Response.status(200).entity(result.toString()).build();
	}
	@POST
	@Path("update")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@HeaderParam("Authorization") String token, 
			@FormDataParam("file") InputStream uploadedInputStream, 
    		@FormDataParam("file") FormDataContentDisposition fileDetail,
    		@FormDataParam("param") String param) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.update(param, this.userId);
		return Response.status(200).entity(result.toString()).build();
	}
	@PUT
	@Path("remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@HeaderParam("Authorization") String token, String qa) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.remove(qa, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}

	@PUT
	@Path("restore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response restore(@HeaderParam("Authorization") String token, String qa) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.restore(qa, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@DELETE
	@Path("delete")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@HeaderParam("Authorization") String token, String qa) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.delete(qa, Integer.parseInt(this.userId));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@POST
	@Path("get-course-by-page")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQAByPage(@HeaderParam("Authorization") String token, String param) {
//		if (!this.isLogined)
//			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.getCourseByPage(param);
		return Response.status(200).entity(result.toString()).build();
	}
	
	@GET
	@Path("get-course-by-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getQAById(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		if (!this.isLogined)
			return Response.status(200).entity(this.notFoundUser().toString()).build();
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.getCourseById(request.getParameter("course_id"));
		return Response.status(200).entity(result.toString()).build();
	}
	
	@GET
	@Path("view-course-by-id")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response viewQAById(@HeaderParam("Authorization") String token, @Context HttpServletRequest request) {
		CourseDao courseDao = (CourseDaoImpl) this.appContext.getBean("courseDao");
		JSONObject result = courseDao.viewCourseById(request.getParameter("course_id"));
		return Response.status(200).entity(result.toString()).build();
	}
}
