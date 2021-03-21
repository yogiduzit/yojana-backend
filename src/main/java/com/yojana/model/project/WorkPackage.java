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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;
import com.yojana.model.employee.Employee;
import com.yojana.model.timesheet.Status;

@Entity
@Table(name = "WorkPackage")
@EntityListeners(AuditListener.class)
public class WorkPackage implements Auditable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7739894210058516672L;
	
	@Embedded
	private Audit audit;
	
	@EmbeddedId
	private WorkPackagePK workPackagePk;
	
	@JoinColumn(name = "ProjectID", insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumns({
		@JoinColumn(name = "ParentWorkPackageID", referencedColumnName = "WorkPackageID", insertable = false, updatable = false),
		@JoinColumn(name = "ProjectID", referencedColumnName = "ProjectID", insertable = false, updatable = false)
	})
	private WorkPackage parentWP;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentWP")
	private Set<WorkPackage> childWPs;
	
	@Column(name = "ParentWorkPackageID", insertable = false, updatable = false)
	private String parentWPId;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ResponsibleEngineerID")
	private Employee responsibleEngineer;
	
	@Column(name = "WorkPackageName")
	private String workPackageName;
	
	@Column(name = "Descrip", columnDefinition="TEXT")
	private String description;
	
	@Column(name = "IsLowestLevel")
	private boolean isLowestLevel;
	
	@Column(name = "AllocatedBudget")
	private float allocatedBudget;
	
	@Column(name = "InitialEstimate")
	private float initialEstimate;
	
	@Column(name = "Charged")
	private float charged;
	
	@Column(name = "CostAtCompletion")
	private float costAtCompletion;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "DueAt")
    private Date dueAt;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "Stat", columnDefinition = "enum")
    private Status status;
	
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

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

}
