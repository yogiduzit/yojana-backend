package com.corejsf.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@Column(name = "EmpUserName", unique = true)
	@NotBlank
    private String username;
	
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
    public Employee(final int empId, final String empName, final String empUserName) {
        this.id = empId;
        this.fullName = empName;
        this.username = empUserName;
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
     * Getter for username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username
     *
     * @param username
     */
    public void setUsername(final String id) {
        username = id;
    }

	public int getId() {
		return id;
	}

	public void setId(int empId) {
		this.id = empId;
	}

}