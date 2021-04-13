package com.yojana.model.project;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;
import com.yojana.model.employee.Employee;

@Entity
@Table(name = "WorkPackage")
@EntityListeners(AuditListener.class)
public class WorkPackage implements Auditable, Serializable, Comparable<WorkPackage> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7739894210058516672L;
	
	@Embedded
	private Audit audit;
	
	@EmbeddedId
	private WorkPackagePK workPackagePk;
	
	@JoinColumn(name = "ProjectID", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumns({
		@JoinColumn(name = "ParentWorkPackageID", referencedColumnName = "WorkPackageID", insertable = false, updatable = false),
		@JoinColumn(name = "ProjectID", referencedColumnName = "ProjectID", insertable = false, updatable = false)
	})
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private WorkPackage parentWP;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentWP")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Set<WorkPackage> childWPs;
	
	@Column(name = "ParentWorkPackageID")
	private String parentWPId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ResponsibleEngineerID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Employee responsibleEngineer;
	
	@Column(name = "ResponsibleEngineerID", insertable = false, updatable = false)
	private Integer responsibleEngineerId;
	
	@Column(name = "WorkPackageName")
	private String workPackageName;
	
	@Column(name = "Descrip", columnDefinition="TEXT")
	private String description;
	
	@Column(name = "IsLowestLevel")
	private Boolean isLowestLevel;
	
	@Column(name = "Budget")
	private Float budget;
	
	@Column(name = "EngineerPlanned")
	private Float planned;
	
	@Column(name = "AllocatedBudget")
	private Float allocatedBudget;
	
	@Column(name = "InitialEstimate")
	private Float initialEstimate;
	
	@Column(name = "AllocatedInitialEstimate")
	private Float allocatedInitialEstimate;
	
	@Column(name = "Charged")
	private Float charged;
	
	@Column(name = "CostAtCompletion")
	private Float costAtCompletion;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "DueAt")
    private Date dueAt;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "Stat", columnDefinition = "enum")
    private WorkPackageStatus status;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinTable(
        name = "EmployeePackage",
        joinColumns = { 
            @JoinColumn(name = "WorkPackageID", referencedColumnName = "WorkPackageID", insertable = false, updatable = false),
        	@JoinColumn(name = "ProjectID", referencedColumnName = "ProjectID", insertable = false, updatable = false)
        }, 
        inverseJoinColumns = { @JoinColumn(name = "EmpID", referencedColumnName = "EmpID") }
    )
    private Set<Employee> employees;
	
	/**
	 * Represents the nesting / hierarchy level of the work package
	 * 0 if it is a top level work package
	 */
	@Column(name = "HierarchyLevel")
	private int hierarchyLevel;
	
	public WorkPackage() {
		
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public WorkPackagePK getWorkPackagePk() {
		return workPackagePk;
	}

	public void setWorkPackagePk(WorkPackagePK workPackagePk) {
		this.workPackagePk = workPackagePk;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public WorkPackage getParentWP() {
		return parentWP;
	}

	public void setParentWP(WorkPackage parentWP) {
		this.parentWP = parentWP;
	}

	public Set<WorkPackage> getChildWPs() {
		return childWPs;
	}

	public void setChildWPs(Set<WorkPackage> childWPs) {
		this.childWPs = childWPs;
	}

	public String getParentWPId() {
		return parentWPId;
	}

	public void setParentWPId(String parentWPId) {
		this.parentWPId = parentWPId;
	}

	public Employee getResponsibleEngineer() {
		return responsibleEngineer;
	}

	public void setResponsibleEngineer(Employee responsibleEngineer) {
		this.responsibleEngineer = responsibleEngineer;
	}

	public String getWorkPackageName() {
		return workPackageName;
	}

	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean isLowestLevel() {
		return isLowestLevel;
	}

	public void setLowestLevel(Boolean isLowestLevel) {
		this.isLowestLevel = isLowestLevel;
	}

	public Float getAllocatedBudget() {
		return allocatedBudget;
	}

	public void setAllocatedBudget(Float allocatedBudget) {
		this.allocatedBudget = allocatedBudget;
	}

	public Float getInitialEstimate() {
		return initialEstimate;
	}

	public void setInitialEstimate(Float initialEstimate) {
		this.initialEstimate = initialEstimate;
	}

	public Float getCharged() {
		return charged;
	}

	public void setCharged(Float charged) {
		this.charged = charged;
	}

	public Float getCostAtCompletion() {
		return costAtCompletion;
	}

	public void setCostAtCompletion(Float costAtCompletion) {
		this.costAtCompletion = costAtCompletion;
	}

	public Date getDueAt() {
		return dueAt;
	}

	public void setDueAt(Date dueAt) {
		this.dueAt = dueAt;
	}

	public WorkPackageStatus getStatus() {
		return status;
	}

	public void setStatus(WorkPackageStatus status) {
		this.status = status;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Float getBudget() {
		return budget;
	}

	public void setBudget(Float budget) {
		this.budget = budget;
	}

	public Float getAllocatedInitialEstimate() {
		return allocatedInitialEstimate;
	}

	public void setAllocatedInitialEstimate(Float allocatedInitialEstimate) {
		this.allocatedInitialEstimate = allocatedInitialEstimate;
	}

	public int getHierarchyLevel() {
		return hierarchyLevel;
	}

	public void setHierarchyLevel(int hierarchyLevel) {
		this.hierarchyLevel = hierarchyLevel;
	}

	public Boolean getIsLowestLevel() {
		return isLowestLevel;
	}

	public void setIsLowestLevel(Boolean isLowestLevel) {
		this.isLowestLevel = isLowestLevel;
	}

	public Float getPlanned() {
		return planned;
	}

	public void setPlanned(Float planned) {
		this.planned = planned;
	}
	
	@PrePersist
	public void initialize() {
	    if (initialEstimate == null) {
	    	initialEstimate = 0.0f;
	    }
	    if (allocatedInitialEstimate == null) {
	    	allocatedInitialEstimate = 0.0f;
	    }
	    if (budget == null) {
	    	budget = 0.0f;
	    }
	    if (allocatedBudget == null) {
	    	allocatedBudget = 0.0f;
	    }
	    if (charged == null) {
	    	charged = 0.0f;
	    }
	    if (costAtCompletion == null) {
	    	costAtCompletion = 0.0f;
	    }
	    if (planned == null) {
	    	planned = 0.0f;
	    }
	}

	public Integer getResponsibleEngineerId() {
		return responsibleEngineerId;
	}

	public void setResponsibleEngineerId(Integer responsibleEngineerId) {
		this.responsibleEngineerId = responsibleEngineerId;
	}

	@Override
	public int compareTo(WorkPackage wp) {
		if (!this.workPackagePk.getProjectID().equals(wp.getWorkPackagePk().getProjectID())) {
			return this.workPackagePk.getProjectID().compareTo(wp.getWorkPackagePk().getProjectID());
		}
		String id1 = this.getWorkPackagePk().getId().substring(2);
		String id2 = wp.getWorkPackagePk().getId().substring(2);
		int lesser = (id1.length() > id2.length()) ? id1.length() : id2.length();
		
		if (this.getHierarchyLevel() == wp.getHierarchyLevel()
				&& this.getHierarchyLevel() == 0) {
			return Integer.parseInt(id1) - Integer.parseInt(id2);
		}
			
		for (int i = 0; i < lesser; i += 2) {
			if (id1.charAt(i) == id2.charAt(i)) continue;
			if (id1.charAt(i) < id2.charAt(i)) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;
	}

}
