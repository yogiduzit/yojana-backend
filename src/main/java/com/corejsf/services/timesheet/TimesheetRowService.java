package com.corejsf.services.timesheet;

import java.net.URI;
import java.sql.SQLException;
import java.util.UUID;

import javax.inject.Inject;
import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.corejsf.access.TimesheetManager;
import com.corejsf.access.TimesheetRowManager;
import com.corejsf.model.timesheet.Timesheet;
import com.corejsf.model.timesheet.TimesheetRow;


@Path("/timesheetrows")
public class TimesheetRowService {
	
	@Inject
	// Provides access to the TimesheetRow table
	private TimesheetRowManager timesheetrowManager;
	
	@Inject
	// Provides access to the TimesheetRow table
	private TimesheetManager timesheetManager;
	
	
	
	@GET
	@Path("/{timesheetid}/{projectid}/{workpackageid}")
	@Produces("application/xml")
	public TimesheetRow find(@PathParam("timesheetid") UUID timesheetId, @PathParam("projectid") UUID projectId, @PathParam("workpackageid") UUID workPackageId) {
		TimesheetRow timesheetrow;
		timesheetrow = timesheetrowManager.find(timesheetId, projectId, workPackageId);
		if (timesheetrow == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return timesheetrow;
	}
	
	@POST
	@Path("/{timesheetid}")
    @Consumes("application/json")
	// Inserts a timesheetrow into the database
	public Response persist(@PathParam("timesheetid") UUID timesheetId, TimesheetRow timesheetrow) throws SQLException {
		timesheetrow.setTimesheet(timesheetManager.find(timesheetId.toString()));
		System.out.println(timesheetrow.getTimesheet().getTsId());
		UUID uuid1 = UUID.randomUUID();
		timesheetrow.setProjectId(uuid1);
		UUID uuid2 = UUID.randomUUID();
		timesheetrow.setWorkPackageId(uuid2);
		timesheetrowManager.persist(timesheetrow);
		return Response.created(URI.create("/timesheetrows/" + timesheetrow.getTimesheetId() + "/" + timesheetrow.getProjectId() + "/" + timesheetrow.getWorkPackageId())).build();
	}
	
	@PATCH
	@Path("{id}")
    @Consumes("application/json")
	// Updates a existing timesheetrow
	public Response merge(@PathParam("id") UUID timesheetid, TimesheetRow timesheetrow) throws SQLException {
		timesheetrowManager.merge(timesheetrow);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("/{id}")
	// Deletes a existing timesheetrow
	public Response remove(TimesheetRow timesheetrow, @PathParam("id") String timesheetId) throws SQLException {
		timesheetrow = find(timesheetrow.getTimesheetId(), timesheetrow.getProjectId(), timesheetrow.getWorkPackageId());
		timesheetrowManager.remove(timesheetrow, timesheetrow.getTimesheetId(), timesheetrow.getProjectId(), timesheetrow.getWorkPackageId());
		return Response.ok().build();
	}
	
	@GET
    @Path("/all")
    @Produces("application/json")
	// Gets a list of all timesheetrows
	public TimesheetRow[] getAll() throws SQLException {
		TimesheetRow[] timesheetrows;
		timesheetrows = timesheetrowManager.getAll();
		if (timesheetrows == null) {
			throw new WebApplicationException("There are no timesheetrows somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheetrows[0].getTimesheet().getTsId());
		return timesheetrows;
	}
	

}
