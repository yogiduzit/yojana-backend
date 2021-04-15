package com.yojana.services.project;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.yojana.access.ProjectManager;

import com.yojana.access.ReportManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;
import com.yojana.response.APIResponse;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Secured
@Path("/reports")
public class ReportService {
    
    @Inject
    private ReportManager reportManager;
    
    @Inject
    private ProjectManager projectManager;
        
    @Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableReports() {
        APIResponse res = new APIResponse();
        
        List<Project> projects = projectManager.getAllForProjectManager(authEmployee.getId());
        res.getData().put("reports", projects);
        
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/earnedValue/{projectId}/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonthlyReport (@PathParam("projectId") String projectId,
    		@PathParam("date") String dateString) {
    	LocalDate date = LocalDate.parse(dateString);
        APIResponse res = new APIResponse();
        
        Map<String, Map<String, Object>> report = reportManager.generateMonthlyReport(projectId, date);
        res.getData().put("report", report);
        
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/weeklyReport/{projectId}/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeeklyReport (@PathParam("projectId") String projectId,
    		@PathParam("date") String dateString) {
    	LocalDate date = LocalDate.parse(dateString);
        APIResponse res = new APIResponse();
        
        Map<String, Map<String, Object>> report = reportManager.generateWeeklyReport(projectId, date);
        res.getData().put("report", report);
        
        return Response.ok().entity(res).build();
    }
    
}
