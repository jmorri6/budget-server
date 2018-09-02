package com.jmorri6.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jmorri6.pojo.ScheduledJob;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;
import com.jmorri6.util.LogConfig;

@Singleton
@Path("/schedules")
public class ScheduleResource {
private static final Logger LOGGER = Logger.getLogger(ScheduleResource.class.getName());

	private IDbHelper dbHelper = new DbHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSchedules() {
		LOGGER.info("Getting scheduledJobs...");
		List<ScheduledJob> results = dbHelper.getSchedueldJobsForDisplay();
		String stuff = new Gson().toJson(results);
		if (results != null && results.size() > 0) {
			return Response.ok().entity(stuff).build();
		}
		
		return Response.noContent().build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addSchedule(String json) {
		LOGGER.info("Got request to add scheduled job");
		
		ScheduledJob job = new Gson().fromJson(json, ScheduledJob.class);
		
		try {
			dbHelper.createScheduledJob(job);
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error adding scheduled job", e));
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteSchedule(@PathParam("id") Integer id) {
		LOGGER.info("Got request to delete");
		try {
			dbHelper.deleteScheduledJob(id);
		} catch (Exception e) {
			LOGGER.severe(LogConfig.format("Error deleting scheduled job", e));
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response updateSchedule(@PathParam("id") Integer id, String json) {
		LOGGER.info("Got request to update schedule");

		return Response.status(409).build();
	}
	
}
