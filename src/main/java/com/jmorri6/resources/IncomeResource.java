package com.jmorri6.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jmorri6.pojo.Income;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;

@Singleton
@Path("/income")
public class IncomeResource {
private static final Logger LOGGER = Logger.getLogger(IncomeResource.class.getName());

	private IDbHelper dbHelper = new DbHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllIncome() {
		LOGGER.info("Getting all income...");
		List<Income> results = dbHelper.getAllIncome();
		
		if (results != null && results.size() > 0) {
			return Response.ok().entity(results).build();
		}
		
		return Response.noContent().build();
	}

	@GET
	@Path("/total")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getIncomePerMonth() {
		LOGGER.info("Getting income per month...");
		List<Income> results = dbHelper.getAllIncome();
		

		return Response.ok().entity(getMonthlyIncome(results).toString()).build();
	}
	
	private Double getMonthlyIncome(List<Income> incomes) {
		double monthly = 0;
		if (incomes != null) {
			for (Income i : incomes) {
				double perYr = i.getAmount() * i.getFrequencyPerYr();
				double perMonth = perYr / 12.0;
				monthly += perMonth - i.getMonthlyDeduction();
			}
		}
		return monthly;
	}

	@GET
	@Path("/unallocated")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUnallocatedIncome() {
		LOGGER.info("Getting income per month...");
		List<Income> results = dbHelper.getAllIncome();
		Double income = getMonthlyIncome(results);
		double allocated = dbHelper.getAllocatedIncome();
		Double unallocated = income - allocated;
		return Response.ok().entity(unallocated.toString()).build();
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addIncome(String json) {
		LOGGER.info("Got request to add income");
		
		Income i = new Gson().fromJson(json, Income.class);
		
		try {
			dbHelper.insertIncome(i);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
	
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response editBudget(@PathParam("id") Integer id) {
		LOGGER.info("Got request to delete");
		try {
			dbHelper.deleteIncome(id);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
		
	}
	
	
	@DELETE
	@Path("/{id}")
	public Response deleteBudget(@PathParam("id") Integer id) {
		return Response.ok().build();
	}
}
