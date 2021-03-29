package com.yojana.services.project;

import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.yojana.access.ProjectManager;
import com.yojana.model.employee.Credential;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.model.project.Report;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

@Path("/reports")
@Secured
public class ReportService {
	
	@Inject
	private ProjectManager projectManager;
		
	@Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;
	
	@GET
    @Path("/{id}")
    @Produces("application/json")
	public Response createReport(String projectId) {
		Project project = projectManager.find(projectId);
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		if (project == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("report", projectId, null));
			 return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		Employee projectManager = project.getProjectManager();
		Credential credential = projectManager.getCredential();
		Report report = new Report();
		report.setId(UUID.randomUUID().toString());
		report.setProjectId(project.getId());
		report.setProjectName(project.getName());
		report.setProjectBudget(project.getBudget());
		report.setProjectInitialEstimate(project.getInitialEstimate());
		report.setProjectDes(project.getDescription());
		report.setStatus(project.getStatus());
		report.setProjectManagerId(projectManager.getId());
		report.setProjectManagerName(projectManager.getFullName());
		report.setProjectManagerUsername(credential.getUsername());
		report.setProjectManagerPassword(credential.getPassword());
		report.setProjectCreatedAt(project.getAudit().getCreatedAt());
		report.setProjectUpdatedAt(project.getAudit().getUpdatedAt());
		res.getData().put("report", report);
		return Response.ok().entity(res).build();
		
	}


	public ProjectManager getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}
	
}
