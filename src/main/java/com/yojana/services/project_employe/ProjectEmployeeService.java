package com.yojana.services.project_employe;

import java.net.URI;
import java.util.ArrayList;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.EmployeeManager;
import com.yojana.access.EstimateManager;
import com.yojana.access.ProjectEmployeeManager;
import com.yojana.access.ProjectManager;
import com.yojana.access.TimesheetManager;
import com.yojana.access.TimesheetRowManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.model.projectemployee.ProjectEmployee;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetRow;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

@Path("/projectEmployee")
@Secured
public class ProjectEmployeeService {
	
	@Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;
	
	@Inject
	private ProjectEmployeeManager projectEmployeeManager;
	
	@Inject
	// Provides access to the employee table
	private EmployeeManager employeeManager;
	
	@Inject
	// Provides access to the project table
	private ProjectManager projectManager;
	
	
    @GET
    @Path("{projectId}/addEmployee/{employeeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response AddEmployeeToProject(@PathParam("projectId") String projectId, @PathParam("employeeId") int employeeId ) {
    	APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
		    return Response.status(Response.Status.FORBIDDEN).entity(res).build();
		}
		
		Project tagetProjectModel = projectManager.find(projectId);
		if (tagetProjectModel == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		
		Employee targetEmpModel = employeeManager.find(employeeId);
		if (targetEmpModel == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		
		ProjectEmployee resultModel = new  ProjectEmployee();
		resultModel.setEmployee(targetEmpModel);
		resultModel.setProject(tagetProjectModel);
		
		projectEmployeeManager.persist(resultModel);
		
		return Response.ok(resultModel).build();
    }
    
    @GET
    @Path("/getAllEmployees/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllEmployeesForProject(@PathParam("projectId") String projectId) {
    	APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
		    return Response.status(Response.Status.FORBIDDEN).entity(res).build();
		}
		
		Project tagetProjectModel = projectManager.find(projectId);
		if (tagetProjectModel == null) {
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		
		List<ProjectEmployee> allPEList = projectEmployeeManager.findAllEmployeesForProject(projectId);
		if(allPEList.size() == 0) {
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		
		List<Employee> resultList = new ArrayList<Employee>();
		
		for(ProjectEmployee item : allPEList) {
			resultList.add(item.getEmployee());
		}
		return Response.ok(resultList).build();
    }
}
