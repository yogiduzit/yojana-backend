package com.corejsf.services.timesheet;

import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.corejsf.access.EmployeeManager;
import com.corejsf.access.TimesheetManager;
import com.corejsf.model.timesheet.Timesheet;
import com.corejsf.response.APIResponse;

@Path("/timesheets")
public class TimesheetService {

	@Inject
	// Provides access to the employee table
	private TimesheetManager timesheetManager;
	
	@Inject
	// Provides access to the employee table
	private EmployeeManager employeeManager;
	
	@GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Finds a timesheet
	public Response find(@PathParam("id") UUID id) throws SQLException {
		Timesheet timesheet;
		timesheet = timesheetManager.find(id);
		if (timesheet == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		Map<String, Object> data = new HashMap<String, Object>();
        data.put("timesheet", timesheet);
        return Response.ok().entity(new APIResponse(data)).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	// Inserts a timesheet in the database
	public Response persist(Timesheet timesheet) throws SQLException {
	    timesheet.setEmployee(employeeManager.find(timesheet.getOwnerId()));
		UUID uuid = UUID.randomUUID();
		timesheet.setId(uuid);
		timesheetManager.persist(timesheet);
		return Response.created(URI.create("/timesheets/" + timesheet.getId())).build();
	}
	

	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	// Updates a existing timesheet
	public Response merge(@PathParam("id") UUID id, Timesheet timesheet) throws SQLException {
	    timesheet.setEmployee(employeeManager.find(timesheet.getOwnerId()));
		timesheetManager.merge(timesheet);
		return Response
                .created(URI.create("/timesheets/" + timesheet.getId()))
                .entity(new APIResponse())
                .build();
	}
	
	@DELETE
	@Path("/{id}")
	// Deletes a existing timesheet
	public Response remove(@PathParam("id") UUID id)throws SQLException {
		timesheetManager.remove(timesheetManager.find(id), id);
		return Response.ok().build();
	}
	
	@GET
    @Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	// Gets a list of all timesheets
	public Response getAll() throws SQLException {
		Timesheet[] timesheets;
		timesheets = timesheetManager.getAll();
		if (timesheets == null) {
			throw new WebApplicationException("There are no timesheets somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheets[0].getEmployee().getId());
		Map<String, Object> data = new HashMap<String, Object>();
        data.put("timesheets", timesheets);
        return Response.ok().entity(new APIResponse(data)).build();
	}
	
	@GET
    @Path("/emp/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Gets a list of all timesheets for an id
	public Response getAllForEmployee(@PathParam("id") UUID id) throws SQLException {
		Timesheet[] timesheets;
		timesheets = timesheetManager.getAllForEmployee(id);
		if (timesheets == null) {
			throw new WebApplicationException("There are no timesheets somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheets[0].getEmployee().getId());
		Map<String, Object> data = new HashMap<String, Object>();
        data.put("timesheets", timesheets);
        return Response.ok().entity(new APIResponse(data)).build();
	}
}
