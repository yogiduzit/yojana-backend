package com.yojana.model.employee;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;
import com.yojana.model.project.Project;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.timesheet.Timesheet;

/**
 * Represents an employee.
 * @author Yogesh Verma
 * @version 1.0
 */

@Entity
@Table(name = "Employee")
@EntityListeners(AuditListener.class)
/* Implements auditable since this entity needs to be time-tracked */
public class Employee implements Auditable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1016424160017320304L;

	@Embedded
	private Audit audit;
	
    /**
     * Represents the number of an employee
     */
	@Id
    @Column(name = "EmpID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
    private String fullName;
	
	/**
	 * FetchType.LAZY means don't load the credential when employees
	 * are requested.
	 * JsonProperty.Access.WRITE_ONLY prevents the LazyInitializationException
	 * This happens because JSON tries to add the credential property as well
	 * but since we have specified it's lazy loaded, it doesn't exist at that moment.
	 * This annotation says "Unless we're trying to do a write action to an employee
	 * , ignore the Credential"
	 */
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "emp", orphanRemoval = true)
	private Credential credential;

  /**
     * Represents the ID of the timesheet approver of this employee
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LabourGrade")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private PayGrade labourGrade;
    
    @Column(name = "LabourGrade", updatable = false, insertable = false)
    private String labourGradeId;
  
    /**
     * Represents the ID of the creator of this employee
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee creator;
    
    @Column(columnDefinition="integer", name = "CreatedBy", updatable = false, insertable = false, nullable = true)
    private Integer creatorId;
    
    /**
     * Represents the ID of the manager of this employee
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagedBy")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee manager;
    
    @Column(columnDefinition="integer", name = "ManagedBy", updatable = false, insertable = false)
    private Integer managerId;
    
    /**
     * Represents the ID of the timesheet approver of this employee
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TimesheetApproverID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee timesheetApprover;
    
    @Column(columnDefinition="integer", name = "TimesheetApproverID", updatable = false, insertable = false)
    private Integer timesheetApproverId;
    
    /**
     * Represents if an employee is a timesheet approver
     */
    @Column(name = "IsTimesheetApprover")
    private boolean isTimesheetApprover;
    
    /**
     * Represents if an employee is in HR
     */
    @Column(name = "IsHR")
    private boolean isHr;
    
    /**
     * Represents if an employee is an admin
     */
    @Column(name = "IsAdmin")
    private boolean isAdmin;
    
    /**
     * Represents if an employee is a project manager
     */
    @Column(name = "IsProjectManager")
    private boolean isProjectManager;
    
    @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Project> projects;
<<<<<<< src/main/java/com/yojana/model/employee/Employee.java
=======
    
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Timesheet> timesheets;
>>>>>>> src/main/java/com/yojana/model/employee/Employee.java
//  
//    @Lob
//    @Basic(fetch = FetchType.LAZY)
//    private byte[] profilePicture;
//
	public Employee() {}

    /**
     * Three parameter constructor. Creates the initial employees who have access as
     * well as the admin
     *
     * @param empNum
     * @param empName
     * @param id
     */
    public Employee(final int empId, final String empName) {
        this.id = empId;
        this.fullName = empName;
    }

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	/**
	 * Get the ID of an employee
	 * @return Id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the ID of an employee
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	/**
	 * Set the full name of an employee
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}
    /**
     * @return the labourGrade
     */
    public PayGrade getLabourGrade() {
        return labourGrade;
    }

    /**
     * @param labourGrade the labourGrade to set
     */
    public void setLabourGrade(PayGrade labourGrade) {
        this.labourGrade = labourGrade;
    }

    /**
     * @return the labourGradeId
     */
    public String getLabourGradeId() {
        return labourGradeId;
    }

    /**
     * @param labourGradeId the labourGradeId to set
     */
    public void setLabourGradeId(String labourGradeId) {
        this.labourGradeId = labourGradeId;
    }

    /**
     * @return the creator
     */
    public Employee getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(Employee creator) {
        this.creator = creator;
    }

    /**
     * @return the creatorId
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId the creatorId to set
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return the manager
     */
    public Employee getManager() {
        return manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    /**
     * @return the managerId
     */
    public Integer getManagerId() {
        return managerId;
    }

    /**
     * @param managerId the managerId to set
     */
    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    /**
     * @return the timesheetApprover
     */
    public Employee getTimesheetApprover() {
        return timesheetApprover;
    }

    /**
     * @param timesheetApprover the timesheetApprover to set
     */
    public void setTimesheetApprover(Employee timesheetApprover) {
        this.timesheetApprover = timesheetApprover;
    }

    /**
     * @return the timesheetApproverId
     */
    public Integer getTimesheetApproverId() {
        return timesheetApproverId;
    }

    /**
     * @param timesheetApproverId the timesheetApproverId to set
     */
    public void setTimesheetApproverId(Integer timesheetApproverId) {
        this.timesheetApproverId = timesheetApproverId;
    }

    /**
     * @return the isTimesheetApprover
     */
    public boolean isTimesheetApprover() {
        return isTimesheetApprover;
    }

    /**
     * @param isTimesheetApprover the isTimesheetApprover to set
     */
    public void setTimesheetApprover(boolean isTimesheetApprover) {
        this.isTimesheetApprover = isTimesheetApprover;
    }

    /**
     * @return the isHr
     */
    public boolean isHr() {
        return isHr;
    }

    /**
     * @param isHr the isHr to set
     */
    public void setHr(boolean isHr) {
        this.isHr = isHr;
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the isProjectManager
     */
    public boolean isProjectManager() {
        return isProjectManager;
    }

    public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	/**
     * @param isProjectManager the isProjectManager to set
     */
    public void setProjectManager(boolean isProjectManager) {
        this.isProjectManager = isProjectManager;
    }

    public Set<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(Set<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	@Override
    public String toString() {
        return "Employee [audit=" + audit + ", id=" + id + ", fullName=" + fullName + ", credential=" + credential
                + ", creator=" + creator + ", creatorId=" + creatorId + ", manager=" + manager + ", managerId="
                + managerId + ", timesheetApprover=" + timesheetApprover + ", timesheetApproverId="
                + timesheetApproverId + ", isTimesheetApprover=" + isTimesheetApprover + ", isHr=" + isHr + ", isAdmin="
                + isAdmin + ", isProjectManager=" + isProjectManager + "]";
    }  
}