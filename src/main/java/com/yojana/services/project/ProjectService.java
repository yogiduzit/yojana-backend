package com.yojana.services.project;

import java.net.URI;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Hibernate;


import com.yojana.access.EmployeeManager;
import com.yojana.access.EstimateManager;
import com.yojana.access.ProjectManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.helpers.ProjectHelper;
import com.yojana.model.employee.Employee;
import com.yojana.model.estimate.Estimate;
import com.yojana.model.project.Project;
import com.yojana.model.project.WorkPackage;
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
	private EstimateManager estimateManager;
	
	@Inject
	// Provides access to the project table
	private ProjectManager projectManager;
	
	@Inject
	private WorkPackageManager wpManager;
	
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
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
		    return Response.status(Response.Status.FORBIDDEN).entity(res).build();
		}
		project.setProjectManager(employeeManager.find(authEmployee.getId()));
		projectManager.persist(project);
		return Response.created(URI.create("/projects/" + project.getId())).entity(res).build();
	}
	
	@PATCH
	@Path("/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	// Inserts a timesheet ain't the database
	public Response merge(@PathParam("id") String id, Project project) { 
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
		    return Response.status(Response.Status.FORBIDDEN).entity(res).build();
		}
		Project old = projectManager.find(id);
		ProjectHelper.patchProject(project, old);
		project.setProjectManager(employeeManager.find(authEmployee.getId()));
		projectManager.merge(old);
		return Response.ok().entity(res).build();
	}
	
	@GET
    @Produces("application/json")
    // Gets a list of all timesheets
    public Response getAll() {
        final APIResponse res = new APIResponse();
        List<Project> projects;
        if(authEmployee.isAdmin() || authEmployee.isProjectManager()) {
            projects = projectManager.getAll();
        } else {
            projects = projectManager.getAllForEmployee(authEmployee.getId());
        }
        
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
	// Finds a project
	public Response find(@PathParam("id") String id) {
		Project project = projectManager.find(id);
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		if (project == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("project",
					id + "",
					null
					));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		Double allocatedBudget = projectManager.getAllocatedBudget(id);
		Double allocatedEstimate = projectManager.getAllocatedInitialEstimate(id);
		
		project.setAllocatedBudget(allocatedBudget);
		project.setAllocatedInitialEstimate(allocatedEstimate);
        res.getData().put("project", project);
        return Response.ok().entity(res).build();
	}
	
	@GET
	@Path("/{id}/workPackages")
	@Produces("application/json")
	public Response getWorkPackages(@PathParam("id") String projectId,
			@QueryParam("hierarchyLevel") Integer hierarchyLevel) {
		APIResponse res = new APIResponse();
		Set<WorkPackage> wps;
		if (hierarchyLevel == null) {
			wps = wpManager.getAll(projectId);
		} else {
			wps = wpManager.getAllWithHierarchyLevel(projectId, hierarchyLevel);
		}
		res.getData().put("workPackages", wps);
		return Response.ok().entity(res).build();
	}
	
	@GET
	@Path("/{id}/workPackages/{wpId}/children")
	@Produces("application/json")
	public Response getChildWPs(@PathParam("id") String projectId, 
			@PathParam("wpId") String wpId) {
		APIResponse res = new APIResponse();
		Set<WorkPackage> wps = wpManager.getChildWPs(projectId, wpId);
		res.getData().put("workPackages", wps);
		return Response.ok().entity(res).build();
	}
    
    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllForProject(@PathParam("projectId") String projectId) {
        final APIResponse res = new APIResponse();
        List<Estimate> estimates = estimateManager.getAllForProject(projectId);
        if (estimates == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("estimate", null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimates", estimates);
        return Response.ok().entity(res).build();
    }

    @GET
    @Path("/get/respEng")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllWPsForResponsibleEngineer() {
        final APIResponse res = new APIResponse();
        Set<WorkPackage> wps = wpManager.getAllForResponsibleEngineer(authEmployee.getId());
        if (wps == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("work package", null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("workPackages", wps);
        return Response.ok().entity(res).build();
    }
    
}
