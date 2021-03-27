package com.yojana.model.project;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embedded;
import org.primefaces.shaded.json.JSONObject;
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
	
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("projectID")
	private String projectId;
	
	@JsonProperty("projectName")
	private String projectName;
	
	@JsonProperty("projectBudget")
	private float projectBudget;
	
	@JsonProperty("projectInitialEstimate")
	private float projectInitialEstimate;
	
	@JsonProperty("projectDescription")
	private String projectDes;
	
	@JsonProperty("projectStatus")
	private ProjectStatus status;
	
	@JsonProperty("projectManagerID")
	private int projectManagerId;
	
	@JsonProperty("projectManagerName")
	private String projectManagerName;
	
	@JsonProperty("projectManagerUsername")
	private String projectManagerUsername;
	
	@JsonProperty("projectManagerPassword")
	private String projectManagerPassword;
	
	@JsonProperty("reportCreatedAt")
	private Date reportCreatedAt;
	
	@JsonProperty("projectCreatedAt")
	private Date projectCreatedAt;
	
	@JsonProperty("projectUpdatedAt")
	private Date projectUpdatedAt;
	
	@JsonProperty("info")
    private JSONObject info;
	
	public Report() {}

	@Override
	public Audit getAudit() {
		return audit;
	}

	@Override
	public void setAudit(Audit audit) {
		this.audit = audit;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public JSONObject getInfo() {
		return info;
	}

	public void setInfo(JSONObject info) {
		this.info = info;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public float getProjectBudget() {
		return projectBudget;
	}

	public void setProjectBudget(float projectBudget) {
		this.projectBudget = projectBudget;
	}
	
	public float getProjectInitialEstimate() {
		return projectInitialEstimate;
	}

	public void setProjectInitialEstimate(float projectInitialEstimate) {
		this.projectInitialEstimate = projectInitialEstimate;
	}
	
	public String getProjectDes() {
		return projectDes;
	}

	public void setProjectDes(String projectDes) {
		this.projectDes = projectDes;
	}
	
	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}
	
	public int getProjectManagerId() {
		return projectManagerId;
	}

	public void setProjectManagerId(int projectManagerId) {
		this.projectManagerId = projectManagerId;
	}
	
	public String getProjectManagerName() {
		return projectManagerName;
	}

	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	
	public String getProjectManagerUsername() {
		return projectManagerUsername;
	}

	public void setProjectManagerUsername(String projectManagerUsername) {
		this.projectManagerUsername = projectManagerUsername;
	}
	
	public String getProjectManagerPassword() {
		return projectManagerPassword;
	}

	public void setProjectManagerPassword(String projectManagerPassword) {
		this.projectManagerPassword = projectManagerPassword;
	}
	
	public Date getReportCreatedAt() {
		return reportCreatedAt;
	}

	public void setReportCreatedAt(Date reportCreatedAt) {
		this.reportCreatedAt = reportCreatedAt;
	}
	
	public Date getProjectCreatedAt() {
		return projectCreatedAt;
	}

	public void setProjectCreatedAt(Date projectCreatedAt) {
		this.projectCreatedAt = projectCreatedAt;
	}
	
	public Date getProjectUpdatedAt() {
		return projectUpdatedAt;
	}

	public void setProjectUpdatedAt(Date projectUpdatedAt) {
		this.projectUpdatedAt = projectUpdatedAt;
	}

}
