package com.corejsf.model.timesheet;

import java.util.UUID;


/**
 * Timesheetrow's id class.
 *
 * @author Anthony Pham
 * @version 1.0
 */
public class TimesheetRowKey implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UUID timesheetId;
	
	private UUID projectId;
	
	private UUID workPackageId;
	
	public TimesheetRowKey() {
		
	}
	
	public TimesheetRowKey(UUID timesheetID, UUID projectID, UUID workPackageID) {
		this.setTimesheetId(timesheetID);
		this.setProjectId(projectID);
		this.setWorkPackageId(workPackageID);
	}

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public UUID getProjectId() {
		return projectId;
	}

	public void setProjectId(UUID projectId) {
		this.projectId = projectId;
	}

	public UUID getWorkPackageId() {
		return workPackageId;
	}

	public void setWorkPackageId(UUID workPackageId) {
		this.workPackageId = workPackageId;
	}
	
	public int hashCode() {
		return Integer.parseInt(timesheetId.toString()) ^ Integer.parseInt(projectId.toString()) ^ Integer.parseInt(workPackageId.toString());
	}
	
	public boolean equals(Object x) {
		return x instanceof TimesheetRowKey &&
				timesheetId == ((TimesheetRowKey) x).getTimesheetId() &&
				projectId == ((TimesheetRowKey) x).getProjectId() &&
				workPackageId == ((TimesheetRowKey) x).getWorkPackageId();
	}
	

}
