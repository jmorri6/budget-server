package com.jmorri6.resources;

import javax.ejb.Singleton;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.jmorri6.pojo.Transaction;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;

@Singleton
@Path("/transactions")
public class TransactionResource {
	private IDbHelper dbHelper = new DbHelper();

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addTransaction(String json) {
		Transaction t = new Gson().fromJson(json, Transaction.class);
		try {
			dbHelper.insertTransaction(t);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		
		return Response.ok().build();
	}
}
