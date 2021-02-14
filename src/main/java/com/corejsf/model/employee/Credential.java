package com.corejsf.model.employee;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents credentials for employees to log in to
 *
 * @author Yogesh Verma
 * @version 1.0
 *
 */

@Entity
@Table(name = "Credential")
@NamedQueries({
	@NamedQuery(name = "Credential.findByUsername", query = "SELECT c FROM Credential c WHERE c.username = :username")
})
public class Credential {

	/**
	 * Represents the id of the employee
	 */
	@Id
	@Column(name = "EmpID", unique = true, columnDefinition = "uuid-char")
	private UUID id;

	/**
	 * Represents the username of the login phase
	 * Foreign key reference to the employee table's EmpID column
	 */
	@PrimaryKeyJoinColumn(name = "EmpID", referencedColumnName= "EmpID")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Employee emp;
	
	/**
	 * Represents the username of the login phase
	 */
	@Column(name = "EmpName")
	private String username;
	
	/**
	 * Represents the password of the login phase
	 */
	@Column(name = "EmpPassword")
	@NotBlank
	private String password;
	
	public Credential() {}
	
	public Credential(String password, String username) {
		this.password = password;
		this.username = username;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID empId) {
		this.id = empId;
	}

	public Employee getEmp() {
		return emp;
	}

	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}