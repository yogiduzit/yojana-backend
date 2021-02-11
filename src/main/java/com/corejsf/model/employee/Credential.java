package com.corejsf.model.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * This class represents credentials for employees and admin to log in to
 *
 * @author Sung Na and Yogesh Verma
 * @version 1.0
 *
 */

@Entity
@Table(name = "Credential")
@NamedQueries({
	@NamedQuery(name = "Credential.findByUsername", query = "SELECT c FROM Credential c WHERE c.emp.username = :username")
})
public class Credential {

	/**
	 * Represents the id of the credentials
	 */

	@Id
	@Column(name = "CredID", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank
	private int credId;

	/**
	 * Represents the username of the login phase
	 * Foreign key reference to the employee table's EmpUserName column
	 */

	@OneToOne(mappedBy="credentials")
	private Employee emp;
	/**
	 * Represents the passowrd of the login phase
	 */

	@Column(name = "EmpPassword")
	@NotBlank
	private String password;

	/**
	 * no parameter constructor
	 */
	public Credential() {
	}

	public Credential(int empNumber, Employee emp, String password) {
		this.emp = emp;
		this.password = password;
	}

	public int getCredId() {
		return credId;
	}

	public void setCredId(int credId) {
		this.credId = credId;
	}
	
	

	public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    /**
	 * Getter for password
	 *
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for password
	 *
	 * @param password
	 */
	public void setPassword(final String pw) {
		password = pw;
	}

}