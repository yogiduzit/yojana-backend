package com.corejsf.model.employee;

/**
 * A model used for authentication.
 * This model is not persisted in the database.
 * @author yogeshverma
 *
 */
public class Authenticator {
	private String username;
	private String password;
	
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
