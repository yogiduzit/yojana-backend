package com.yojana.model.project;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.Auditable;


public class Report implements Auditable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Embedded
	private Audit audit;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Frame")
	private String frame;
	
	@JsonProperty("Date")
	private Date reportCreatedAt;
	
	@JsonProperty("ProjectID")
	private String projectId;
	
	@JsonProperty("Data")
	List<WorkPackage> data;
	
	
	public Report() {}

	@Override
	public Audit getAudit() {
		return audit;
	}

	@Override
	public void setAudit(Audit audit) {
		this.audit = audit;
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
	
	public List<WorkPackage> getData() {
		return data;
	}

	public void setData(List<WorkPackage> data) {
		this.data = data;
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

}
