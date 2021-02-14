package com.corejsf.model.employee;

import java.util.UUID;

import javax.persistence.CascadeType;
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
     * Represents the employee id
     */
	@Id
    @Column(name = "EmpID", unique = true, columnDefinition = "uuid", updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
    private UUID id;
	
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
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "employee")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Credential credentials;
	
    /**
     * Represents the first name of the employee
     */
	
	@Column(name = "EmpName")
	@NotBlank
    private String fullName;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Credential getCredentials() {
		return credentials;
	}

	public void setCredentials(Credential credentials) {
		this.credentials = credentials;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

    
}