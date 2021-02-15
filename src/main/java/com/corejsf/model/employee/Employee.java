package com.corejsf.model.employee;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

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
     * Represents the number of an employee
     */
	@Id
    @Column(name = "EmpID", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
    /**
     * Represents the user name of the employee
     */
	
	@Column(name = "EmpUserName")
	@NotBlank
	private String username;
	
	/**
	 * Represents the credentials for an employee.
	 * Whenever a GET request is made to employee endpoint,
	 * the credentials are lazy loaded, so that we can access them
	 * on demand, which saves time.
	 * The access property ensures that a request to lazy-load the
	 * credentials isn't made.
	 */
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employee")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Credential credentials;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
	@NotBlank
    private String fullName;

	/**
	 * Get the ID of an employee
	 * @return Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the ID of an employee
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the username of an employee
	 * @return string
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the username of an employee
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the credentials of an employee
	 * @return Credentials POJO
	 */
	public Credential getCredentials() {
		return credentials;
	}

	/**
	 * Get the credentials of an employee
	 * @param credentials
	 */
	public void setCredentials(Credential credentials) {
		this.credentials = credentials;
	}

	/**
	 * Get the full name of an employee
	 * @return
	 */
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

    
}