package com.corejsf.model.employee;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Represents an employee.
 *
 * @author Yogesh Verma
 * @version 1.0
 */

@Entity
@Table(name = "Employee")
public class Employee {
	
    /**
     * Represents the employee id
     */
	@Id
    @Column(name = "EmpID", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Type(type="uuid-char")
    private UUID id;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
    private String fullName;
	
	@OneToOne(mappedBy = "emp", fetch = FetchType.LAZY)
	private Credential credential;

    /**
     * no parameter constructor
     */
    public Employee() {
    }

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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}
}