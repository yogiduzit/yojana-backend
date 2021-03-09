package com.yojana.model.employee;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;

/**
 * Represents an employee.
 *
 * @author Yogesh Verma
 * @version 1.0
 */

@Entity
@Table(name = "Employee")
@EntityListeners(AuditListener.class)
public class Employee implements Auditable {
	
	@Embedded
	private Audit audit;
	
    /**
     * Represents the number of an employee
     */
	@Id
    @Column(name = "EmpID", unique = true, columnDefinition = "uuid-char")
	@Type(type="uuid-char")
	/**
	 * Do not use GeneratedValue annotation for UUID.
	 * Instead, use UUID.randomUUID while persisting entities.
	 */
    private UUID id;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
    private String fullName;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "emp", orphanRemoval = true)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Credential credential;
	
	public Employee() {}

    /**
     * Three parameter constructor. Creates the initial employees who have access as
     * well as the admin
     *
     * @param empNum
     * @param empName
     * @param id
     */
    public Employee(final UUID empId, final String empName) {
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
	public UUID getId() {
		return id;
	}

	/**
	 * Set the ID of an employee
	 * @param id
	 */
	public void setId(UUID id) {
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

    
}