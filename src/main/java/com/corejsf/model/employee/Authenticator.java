package com.corejsf.model.employee;

/**
 * A model used for authentication.
 * This model is not persisted in the database.
 * @author Yogesh Verma
 *
 */
public class Authenticator {
	
	/**
	 * Username of an employee
	 */
	private String username;
	
	/**
	 * Password of an employee
	 */
	private String password;
	
	/**
	 * Get the username
	 * @return string
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set the username
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Get the password
	 * @return string
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the password
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
}
