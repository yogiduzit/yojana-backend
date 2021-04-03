package com.yojana.services.project;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import com.yojana.access.ProjectManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.model.project.Report;
import com.yojana.model.project.WorkPackage;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;
import java.util.List;

@Secured
@Path("/reports")
public class ReportService {
    
    @Inject
    private ProjectManager projectManager;
    
    @Inject
    private WorkPackageManager wpManager;
        
    @Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;
    
    @GET
    @Path("/earnedValue/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReport (@PathParam("projectId") String projectId) {
        Project project = projectManager.find(projectId);
        APIResponse res = new APIResponse();
        if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
        if (project == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundSingle("report", projectId, null));
             return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        Report report = new Report();
        report.setType("status");
        report.setFrame("weekly");
        report.setReportCreatedAt(project.getAudit().getCreatedAt());
        report.setProjectId(project.getId());
        List<WorkPackage> data = wpManager.getAll(projectId); 
        report.setData(data);
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
