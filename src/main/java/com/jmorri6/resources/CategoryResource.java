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

import com.jmorri6.pojo.Category;
import com.jmorri6.util.DbHelper;
import com.jmorri6.util.IDbHelper;

@Singleton
@Path("/categories")
public class CategoryResource {
	private static final Logger LOGGER = Logger.getLogger(CategoryResource.class.getName());
	private IDbHelper dbHelper = new DbHelper();
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCategories() {
		LOGGER.info("Getting all categories...");
		List<Category> results = dbHelper.getAllCategories();
		
		if (results != null && results.size() > 0) {
			return Response.ok().entity(results).build();
		}
		
		return Response.noContent().build();
	}
	
	@POST
	@Path("/{name}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response addCategory(@PathParam("name") String name) {
		try {
			dbHelper.addCategory(name);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response deleteCategory(@PathParam("id") Integer id) {
		try {
			dbHelper.deleteCategory(id);
		} catch (Exception e) {
			return Response.status(409).build();
		}
		return Response.ok().build();
	}
}
