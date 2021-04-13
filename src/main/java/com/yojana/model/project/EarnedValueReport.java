package com.yojana.model.project;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Embedded;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.Auditable;


public class EarnedValueReport implements Serializable {

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
	
	
	public EarnedValueReport() {
		this.type = "Earned Value";
		this.frame = "Monthly";
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
