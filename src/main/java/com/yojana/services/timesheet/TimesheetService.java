package com.yojana.services.timesheet;

import java.net.URI;
import java.util.List;
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
import javax.ws.rs.core.Response;

import com.yojana.access.EmployeeManager;
import com.yojana.access.ProjectManager;
import com.yojana.access.TimesheetManager;
import com.yojana.access.TimesheetRowManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetRow;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

@Path("/timesheets")
@Secured
public class TimesheetService {
	
	@Inject
	private ProjectManager projectManager;

	@Inject
	// Provides access to the employee table
	private TimesheetManager timesheetManager;
	
	@Inject
	private TimesheetRowManager timesheetRowManager;
	
	@Inject
	private WorkPackageManager workPackageManager;

	@Inject
	// Provides access to the employee table
	private EmployeeManager employeeManager;
	
	@Inject
	@AuthenticatedEmployee
	private Employee authEmployee;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	// Finds a timesheet
	public Response find(@PathParam("id") UUID id) {
		Timesheet timesheet = timesheetManager.find(id);
		APIResponse res = new APIResponse();
		if (timesheet == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("timesheet", id.toString(), null));
			 return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		res.getData().put("timesheet", timesheet);
		return Response.ok().entity(res).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	// Inserts a timesheet ain't the database
	public Response persist(Timesheet timesheet) {
		APIResponse res = new APIResponse();
		timesheet.setOwnerId(authEmployee.getId());
		if (timesheet.getOwnerId() > 0) {
			final Employee emp = employeeManager.find(timesheet.getOwnerId());
			if (emp == null) {
				ErrorMessageBuilder.notFound("Could not find employee for timesheet", null);
				return Response.status(Response.Status.NOT_FOUND).entity(res).build();
			}
			timesheet.setEmployee(emp);
		}
		UUID uuid = UUID.randomUUID();
		timesheet.setId(uuid);
		timesheetManager.persist(timesheet);
		res.getData().put("id", timesheet.getId());
		return Response.created(URI.create("/timesheets/" + timesheet.getId())).entity(res).build();
	}

	@PATCH
	@Path("{id}")
	@Consumes("application/json")
	@Produces("application/json")
	// Updates a existing timesheet
	public Response merge(@PathParam("id") UUID id, Timesheet timesheet) {
		APIResponse res = new APIResponse();
		if (!id.equals(timesheet.getId())) { 
			res.getErrors().add(ErrorMessageBuilder.badRequest("TimesheetID in route doesn't match timesheet id in body", null));
			return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
		}
		final Timesheet old = timesheetManager.find(id);
		if (old == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("timesheet", id.toString(), null));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		if (timesheet.getOwnerId() > 0) {
			timesheet.setEmployee(employeeManager.find(old.getOwnerId()));
		}
		timesheetManager.merge(timesheet);
		return Response.ok().entity(res).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces("application/json")
	// Deletes a existing timesheet
	public Response remove(@PathParam("id") UUID id) {
		final APIResponse res = new APIResponse();
		timesheetManager.remove(id);
		return Response.ok().entity(res).build();
	}

	@GET
	@Produces("application/json")
	// Gets a list of all timesheets
	public Response getAll() {
		final APIResponse res = new APIResponse();
		List<Timesheet> timesheets = timesheetManager.getAll();
		if (timesheets == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("timesheet", null));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		res.getData().put("timesheets", timesheets);
		return Response.ok().entity(res).build();
	}
	
	@POST
	@Path("/{id}/rows")
    @Consumes("application/json")
	@Produces("application/json")
	// Inserts a timesheetrow into the database
	public Response addRow(@PathParam("id") UUID timesheetId, TimesheetRow row) {
		final APIResponse res = new APIResponse();
		
		row.setTimesheetId(timesheetId);
		row.setTimesheet(timesheetManager.find(timesheetId));		
		row.setProject(projectManager.find(row.getProjectId()));
		row.setWorkPackage(workPackageManager.find(
			new WorkPackagePK(row.getWorkPackageId(), row.getProjectId())
		));
		
		timesheetRowManager.persist(row);
		return Response.created(URI.create("/timesheets/" + row.getTimesheetId() + "/rows/"
			+ "project/" + row.getProjectId() + "/wp/" + row.getWorkPackageId()))
				.entity(res)
				.build();
	}
	
	@GET
    @Path("/{id}/rows")
    @Produces("application/json")
	// Gets a list of all timesheetrows for a timesheet
	public Response getAllForTimesheet(@PathParam("id") UUID timesheetId) {
		APIResponse res = new APIResponse();
		List<TimesheetRow> timesheetRows = timesheetRowManager.getAllForTimesheet(timesheetId); 
        if (timesheetRows == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("timesheetRows", "No rows found for" + 
            	"timesheet: " + timesheetId));
             return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("timesheetRows", timesheetRows);
        return Response.ok().entity(res).build();
	}
}
