package com.corejsf.services;

import java.net.URI;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.TimesheetManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.model.timesheet.Timesheet;

@Path("/timesheets")
public class TimesheetService {

	@Inject
	// Provides access to the employee table
	private TimesheetManager timesheetManager;
	
	@GET
    @Path("/{id}")
    @Produces("application/xml")
	// Finds an employee
	public Timesheet find(@PathParam("id") String id) throws SQLException {
		Timesheet timesheet;
		timesheet = timesheetManager.find(id);
		if (timesheet == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return timesheet;
	}
	
	@POST
    @Consumes("application/xml")
	// Inserts an employee in the database
	public Response persist(Timesheet timesheet) throws SQLException {
		timesheetManager.persist(timesheet);
		return Response.created(URI.create("/employees/" + timesheet.getId())).build();
	}
	

	@PATCH
	@Path("/update")
    @Consumes("application/xml")
	// Updates an existing employee
	public Response merge(Timesheet timesheet) throws SQLException {
		timesheetManager.merge(timesheet);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	// Deletes an existing employee
	public Response remove(Timesheet timesheet, @PathParam("id") String empId) throws SQLException {
		timesheetManager.remove(timesheet, empId);
		return Response.ok().build();
	}
	
	@GET
    @Path("all")
    @Produces("application/json")
	// Gets a list of all employees
	public Timesheet[] getAll() throws SQLException {
		Timesheet[] timesheets;
		timesheets = timesheetManager.getAll();
		if (timesheets == null) {
			throw new WebApplicationException("There are no timesheets somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheets[0].getId());
		return timesheets;
	}
}
