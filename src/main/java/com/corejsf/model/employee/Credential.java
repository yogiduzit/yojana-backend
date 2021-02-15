package com.corejsf.model.employee;

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
public class Credential {

	/**
	 * Represents the id of the credentials
	 */
	@Id
	@Column(name = "CredID", unique = true, columnDefinition = "uuid", updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID id;

	/**
	 * Represents the username of the login phase
	 * Foreign key reference to the credential table's EmpID column
	 */
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmpID")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Employee employee;

	/**
	 * Represents the password of the employee's credentials
	 */
	@Column(name = "EmpPassword")
	@NotBlank
	private String password;

	public UUID getId() {
		return id;
	}

	public void setId(UUID credId) {
		this.id = credId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}