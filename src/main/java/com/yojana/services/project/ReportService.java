package com.yojana.services.project;

import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import com.yojana.access.ProjectManager;
import com.yojana.model.employee.Credential;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.model.project.Report;
import com.yojana.response.APIResponse;
import com.yojana.security.annotations.Secured;

@Path("/reports")
@Secured
public class ReportService {
	
	@Inject
	private ProjectManager projectManager;
		
	public Response createReport(String projectId) {
		Project project = projectManager.find(projectId);
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
		APIResponse res = new APIResponse();
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
