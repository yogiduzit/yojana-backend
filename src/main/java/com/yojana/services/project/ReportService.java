package com.yojana.services.project;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import com.yojana.access.ProjectManager;
import com.yojana.access.TimesheetManager;
import com.yojana.access.TimesheetRowManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.model.project.EarnedValueReport;
import com.yojana.model.project.Project;
import com.yojana.model.project.WeeklyStatusReport;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetRow;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.Secured;

import java.util.ArrayList;
import java.util.List;

@Secured
@Path("/reports")
public class ReportService {
	
	@Inject
	private ProjectManager projectManager;
	
	@Inject
	private TimesheetManager timesheetManager;
	
	@Inject
	private TimesheetRowManager timesheetrowManager;
	
	@Inject
	private WorkPackageManager wpManager;
		
	@GET
	@Path("/earnedValue/{projectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEarnedValueReport (@PathParam("projectId") String projectId) {
		Project project = projectManager.find(projectId);
		APIResponse res = new APIResponse();
		if (project == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("report", projectId, null));
			 return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		EarnedValueReport report = new EarnedValueReport();
		report.setReportCreatedAt(project.getAudit().getCreatedAt());
		report.setProjectId(project.getId());
        List<WorkPackage> data = wpManager.getAll(projectId); 
        report.setData(data);
		res.getData().put("report", report);
		return Response.ok().entity(res).build();
		
	}
	
	@GET
	@Path("/weeklyStatus/{projectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWeeklyStatusReport(@PathParam("projectId") String projectId) {
		Project project = projectManager.find(projectId);
		APIResponse res = new APIResponse();
		if (project == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("report", projectId, null));
			 return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		WeeklyStatusReport report = new WeeklyStatusReport();
		report.setReportCreatedAt(project.getAudit().getCreatedAt());
		report.setProjectId(project.getId());
        List<WorkPackage> data = wpManager.getAll(projectId); 
        report.setData(data);
//        List<List<Timesheet>> timesheets = new ArrayList<List<Timesheet>>();
//        for (int i = 0; i < data.size(); i++) {
//        	List<Timesheet> k = timesheetManager.getTimesheetsForWorkPackage(data.get(i).getWorkPackagePk());
//        	timesheets.add(k);
//        }
//        report.setTimesheets(timesheets);
        
      List<List<TimesheetRow>> timesheetrows = new ArrayList<List<TimesheetRow>>();
      for (int i = 0; i < data.size(); i++) {
      	List<TimesheetRow> k = timesheetrowManager.getAllForWorkPackage(data.get(i).getWorkPackagePk());
      	timesheetrows.add(k);
      }
      report.setTimesheetrows(timesheetrows);
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
