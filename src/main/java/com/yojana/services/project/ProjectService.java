package com.yojana.services.project;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.EmployeeManager;
import com.yojana.access.ProjectManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

@Path("/projects")
@Secured
public class ProjectService {
	
	@Inject
	// Provides access to the employee table
	private EmployeeManager employeeManager;
	
	@Inject
	// Provides access to the project table
	private ProjectManager projectManager;
	
	@Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee
    private Employee authEmployee;

	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	// Inserts a timesheet ain't the database
	public Response persist(Project project) {
		APIResponse res = new APIResponse();
		project.setProjectManager(employeeManager.find(authEmployee.getId()));
		projectManager.persist(project);
		return Response.created(URI.create("/projects/" + project.getId())).entity(res).build();
	}
	
	@GET
	@Produces("application/json")
	// Gets a list of all timesheets
	public Response getAll() {
		final APIResponse res = new APIResponse();
		List<Project> projects = projectManager.getAll();
		if (projects == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("project", null));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		res.getData().put("projects", projects);
		return Response.ok().entity(res).build();
	}

	@GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Finds an employee
	public Response find(@PathParam("id") String id) {
		Project project = projectManager.find(id);
		APIResponse res = new APIResponse();
		if (project == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("project",
					id + "",
					null
					));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
        res.getData().put("project", project);
        return Response.ok().entity(res).build();
	}
}
