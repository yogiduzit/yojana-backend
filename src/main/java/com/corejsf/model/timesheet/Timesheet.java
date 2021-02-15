package com.corejsf.model.timesheet;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Type;

import com.corejsf.model.employee.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a timesheet.
 *
 * @author Adam Krawchuk
 * @version 1.0
 */

@Entity
@Table(name = "Timesheet")
public class Timesheet {
	
    /**
     * Represents the timesheet id
     */
	@Id
    @Column(name = "TimesheetID", unique = true, columnDefinition = "uuid", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type="uuid-char")
    private UUID id;
	
    /**
     * Represents the ID of the employee
     */
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EmpID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employee employee;

    /**
     * Represents the end of the week
     */
	@Column(name = "Endweek")
	@NotBlank
    private LocalDate endWeek;

	/**
     * Represents the ID of the reviewer
     */
    @Column(name = "ReviewedBy")
    private int reviewerID;
    
    /**
     * Represents the signature of the employee
     */
    @Column(name = "Signature")
    private String signature;
    
    /**
     * Represents the feedback of the reviewer
     */
    @Column(name = "Feedback")
    private String feedback;
    
    /**
     * Represents the status of the timesheet
     */
    @Column(name = "Status")
    private Status status;
    
    /**
     * Represents the overtime
     */
    @Column(name = "Overtime")
    private int overtime;
    
    /**
     * Represents the flextime
     */
    @Column(name = "Flextime")
    private int flextime;
    
    /**
     * The time it was last updated
     */
    @Column(name = "UpdatedAt")
    private LocalDate updatedAt;
    
    /**
     * The time it was approved
     */
    @Column(name = "ApprovedAt")
    private LocalDate approvedAt;
    
    /**
     * no parameter constructor
     */
    public Timesheet() {
    }

    /**
     * @param employee
     * @param endWeek
     */
    public Timesheet(Employee employee, @NotBlank LocalDate endWeek) {
        super();
        this.employee = employee;
        this.endWeek = endWeek;
    }

    /**
     * @return the updatedAt
     */
    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt the updatedAt to set
     */
    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return the approvedAt
     */
    public LocalDate getApprovedAt() {
        return approvedAt;
    }

    /**
     * @param approvedAt the approvedAt to set
     */
    public void setApprovedAt(LocalDate approvedAt) {
        this.approvedAt = approvedAt;
    }

    /**
     * @return the overtime
     */
    public int getOvertime() {
        return overtime;
    }

    /**
     * @param overtime the overtime to set
     */
    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    /**
     * @return the flextime
     */
    public int getFlextime() {
        return flextime;
    }

    /**
     * @param flextime the flextime to set
     */
    public void setFlextime(int flextime) {
        this.flextime = flextime;
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the endWeek
     */
    public LocalDate getEndWeek() {
        return endWeek;
    }

    /**
     * @param endWeek the endWeek to set
     */
    public void setEndWeek(LocalDate endWeek) {
        this.endWeek = endWeek;
    }

    /**
     * @return the reviewerID
     */
    public int getReviewerID() {
        return reviewerID;
    }

    /**
     * @param reviewerID the reviewerID to set
     */
    public void setReviewerID(int reviewerID) {
        this.reviewerID = reviewerID;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}