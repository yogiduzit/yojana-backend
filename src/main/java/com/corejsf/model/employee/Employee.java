package com.corejsf.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
	@NotBlank
    private int id;
	
    /**
     * Represents the user name of the employee
     */
	
	@Column(name = "EmpUserName")
	@NotBlank
	private String username;
	
	/**
	 * Credentials use the employee username as a reference
	 * for the foreign key
	 */
	@OneToOne
	@JoinColumn(name = "EmpUserName", referencedColumnName = "EmpUserName")
	@NotBlank
    private Credential credentials;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
	@NotBlank
	
    private String fullName;

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
    public Employee(final int empId, final String empName, final Credential credentials) {
        this.id = empId;
        this.fullName = empName;
        this.credentials = credentials;
    }

    /**
     * Getter for employee full name
     *
     * @return fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Setter for employee full name
     *
     * @param fullName
     */
    public void setFullName(final String empName) {
        fullName = empName;
    }

    /**
     * Getter for employee credentials
     * @return credentials
     */
	public Credential getCredentials() {
        return credentials;
    }

	/**
	 * Setter for employee credentials
	 * @param credentials
	 */
    public void setCredentials(Credential credentials) {
        this.credentials = credentials;
    }

    /**
     * Getter of employee ID
     * @return id
     */
    public int getId() {
		return id;
	}

    /**
     * Setter of employee ID
     * @param empId
     */
	public void setId(int empId) {
		this.id = empId;
	}

	/**
	 * Getter of employee user name
	 * @return username
	 */
    public String getUsername() {
        return username;
    }

    /**
     * Setter of employee user name
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}