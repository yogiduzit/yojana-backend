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


@Path("/timesheets")
public class TimesheetRowService {
	
	@Inject
	// Provides access to the TimesheetRow table
	private TimesheetRowManager timesheetrowManager;
	
	@Inject
	// Provides access to the TimesheetRow table
	private TimesheetManager timesheetManager;
	
	
	@POST
	@Path("/{timesheetID}/rows")
    @Consumes("application/json")
	// Inserts a timesheetrow into the database
	public Response persist(@PathParam("timesheetID") int timesheetId, TimesheetRow timesheetrow) throws SQLException {
		timesheetrow.setTimesheet(timesheetManager.find(String.valueOf(timesheetId)));
		System.out.println(timesheetrow.getTimesheet().getTsId());
		UUID uuid1 = UUID.randomUUID();
		timesheetrow.setProjectId(Integer.parseInt(String.valueOf(uuid1)));
		UUID uuid2 = UUID.randomUUID();
		timesheetrow.setWorkPackageId(String.valueOf(uuid2));
		timesheetrowManager.persist(timesheetrow);
		return Response.created(URI.create("/timesheetrows/" + timesheetrow.getTimesheet().getTsId() + "/" + timesheetrow.getProjectId() + "/" + timesheetrow.getWorkPackageId())).build();
	}
	
	@GET
    @Path("/{timesheetID}/rows")
    @Produces("application/json")
	// Gets a list of all timesheetrows for a timesheet
	public TimesheetRow[] getAllForTimesheet(@PathParam("timesheetID") int timesheetId) throws SQLException {
		TimesheetRow[] timesheetrows;
		timesheetrows = timesheetrowManager.getAllForTimesheet(timesheetId);
		if (timesheetrows == null) {
			throw new WebApplicationException("There are no timesheetrows somehow", Response.Status.NOT_FOUND);
		}
		System.out.println("heyo " + timesheetrows[0].getTimesheet().getTsId());
		return timesheetrows;
	}
	

}
