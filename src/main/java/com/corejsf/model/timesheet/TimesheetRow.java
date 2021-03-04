package com.corejsf.model.timesheet;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Represents a timesheetrow.
 *
 * @author Anthony Pham
 * @version 1.0
 */
@Entity
@Table(name = "TimesheetRow")
@IdClass(TimesheetRowKey.class)
public class TimesheetRow {
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TimesheetID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Timesheet timesheet;
	
	@Id
	@Column(name = "TimesheetID", updatable = false, insertable = false)
	@Type(type="uuid-char")
	private UUID timesheetId;
	
	@Id
    @Column(name = "ProjectID", unique = true, columnDefinition = "uuid-char", updatable = false)
	@Type(type="uuid-char")
    private UUID projectId;
	
	@Id
    @Column(name = "WorkPackageID", unique = true, columnDefinition = "uuid-char", updatable = false)
	@Type(type="uuid-char")
    private UUID workPackageId;
	
	@Column(name = "Notes")
	private String notes;
	
	@Column(name = "Hours", columnDefinition = "FLOAT(5,2)")
	private double hours;
	
	public TimesheetRow() {
		
	}
	
	public TimesheetRow(Timesheet timesheet, UUID projectId, UUID workPackageId, String notes, double hours) {
		super();
		this.timesheet = timesheet;
		this.projectId = projectId;
		this.workPackageId = workPackageId;
		this.notes = notes;
		this.hours = hours;
	}
	
	public Timesheet getTimesheet() {
		return timesheet;
	}
	
	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
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
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public double getHours() {
		return hours;
	}
	
	public void setHours(double hours) {
		this.hours = hours;
	}


}
