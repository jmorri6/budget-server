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
import com.jmorri6.pojo.Budget;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;

@Singleton
@Path("/budgets")
public class BudgetResource {
private static final Logger LOGGER = Logger.getLogger(BudgetResource.class.getName());

	private IDbHelper dbHelper = new DbHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getBudgets() {
		LOGGER.info("Getting all budgets...");
		List<Budget> results = dbHelper.getAllBudgets();
		
		if (results != null && results.size() > 0) {
			return Response.ok().entity(results).build();
		}
		
		return Response.noContent().build();
	}
	
	@GET
	@Path("/balance")
	public Response getBalance() {
		LOGGER.info("Getting budget balance...");
		Double balance = dbHelper.getBudgetBalance();
		return Response.ok(balance.toString()).build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addBudget(String json) {
		LOGGER.info("Got request to add budget");
		
		Budget b = new Gson().fromJson(json, Budget.class);
		
		try {
			dbHelper.addBudget(b);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response editBudget(@PathParam("id") Integer id, String json) {
		LOGGER.info("Got request to edit budget");
		Budget b = new Gson().fromJson(json, Budget.class);
		
		try {
			dbHelper.updateBudget(b);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
		
	}
	
	@GET
	@Path("/{id}")
	public Response deleteBudget(@PathParam("id") Integer id) {
		LOGGER.info("Got request to delete budget");
		try {
			
			dbHelper.deleteBudget(id);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
}
