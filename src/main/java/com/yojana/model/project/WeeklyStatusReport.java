package com.yojana.model.project;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.timesheet.TimesheetRow;

public class WeeklyStatusReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Frame")
	private String frame;
	
	@JsonProperty("Date")
	private Date reportCreatedAt;
	
	@JsonProperty("ProjectID")
	private String projectId;
	
	@JsonProperty("Data")
	Set<WorkPackage> data;
	
	
	@JsonProperty("TimesheetRows")
	List<List<TimesheetRow>> timesheetrows;
//	@JsonProperty("Timesheets")
//	List<List<Timesheet>> timesheets;
	
	public WeeklyStatusReport() {
		this.type = "Status";
		this.frame = "Weekly";
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public Date getReportCreatedAt() {
		return reportCreatedAt;
	}

	public void setReportCreatedAt(Date reportCreatedAt) {
		this.reportCreatedAt = reportCreatedAt;
	}
	
	public Set<WorkPackage> getData() {
		return data;
	}

	public void setData(Set<WorkPackage> data) {
		this.data = data;
	}
	
//	public List<List<Timesheet>> getTimesheets() {
//		return timesheets;
//	}
//
//	public void setTimesheets(List<List<Timesheet>> timesheets) {
//		this.timesheets = timesheets;
//	}
	
	public List<List<TimesheetRow>> getTimesheetrows() {
	return timesheetrows;
}

public void setTimesheetrows(List<List<TimesheetRow>> timesheetrows) {
	this.timesheetrows = timesheetrows;
}

}
