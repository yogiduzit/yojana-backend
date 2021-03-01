package com.corejsf.services.timesheet;

import java.net.URI;
import java.sql.SQLException;
import java.util.UUID;

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

import com.corejsf.access.EmployeeManager;
import com.corejsf.access.TimesheetManager;
import com.corejsf.model.timesheet.Timesheet;

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
    @Produces("application/json")
	// Finds a timesheet
	public Timesheet find(@PathParam("id") String id) throws SQLException {
		Timesheet timesheet;
		timesheet = timesheetManager.find(id);
		if (timesheet == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return timesheet;
	}
	
	@POST
	@Path("{id}")
    @Consumes("application/json")
	// Inserts a timesheet ain the database
	public Response persist(@PathParam("id") UUID id, Timesheet timesheet) throws SQLException {
		timesheet.setEmployee(employeeManager.find(id));
		System.out.println(timesheet.getEmployee().getFullName());
		UUID uuid = UUID.randomUUID();
		timesheet.setTsId(uuid);
		timesheetManager.persist(timesheet);
		return Response.created(URI.create("/timesheets/" + timesheet.getTsId())).build();
	}
	

	@PATCH
	@Path("{id}")
    @Consumes("application/json")
	// Updates a existing timesheet
	public Response merge(@PathParam("id") UUID id, Timesheet timesheet) throws SQLException {
		timesheetManager.merge(timesheet);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	// Deletes a existing timesheet
	public Response remove(Timesheet timesheet, @PathParam("id") String empId) throws SQLException {
		timesheetManager.remove(timesheet, empId);
		return Response.ok().build();
	}
	
	@GET
    @Path("/all")
    @Produces("application/json")
	// Gets a list of all timesheets
	public Timesheet[] getAll() throws SQLException {
		Timesheet[] timesheets;
		timesheets = timesheetManager.getAll();
		if (timesheets == null) {
			throw new WebApplicationException("There are no timesheets somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheets[0].getEmployee().getId());
		return timesheets;
	}
	
	@GET
    @Path("/emp/{id}")
    @Produces("application/json")
	// Gets a list of all timesheets for an id
	public Timesheet[] getAllForEmployee(@PathParam("id") String empId) throws SQLException {
		Timesheet[] timesheets;
		timesheets = timesheetManager.getAllForEmployee(empId);
		if (timesheets == null) {
			throw new WebApplicationException("There are no timesheets somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheets[0].getEmployee().getId());
		return timesheets;
	}
}
