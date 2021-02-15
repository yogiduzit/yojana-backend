package com.corejsf.services.employee;

import java.sql.SQLException;

import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.corejsf.access.EmployeeManager;
import com.corejsf.helpers.JWTHelper;
import com.corejsf.model.employee.Authenticator;
import com.corejsf.model.employee.Credential;

@Path("/authentication")
public class AuthenticationService {

	@Inject
	private EmployeeManager empManager;

	// Helper for password encryption
	private JWTHelper jwtHelper;

	// Provides authentication support
	public AuthenticationService() {
		jwtHelper = new JWTHelper();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Authenticates a user
	public Response authenticateUser(Authenticator auth) {
		try {
			authenticate(auth.getUsername(), auth.getPassword());

			final String token = jwtHelper.encrypt(auth.getUsername());
			return Response.ok(token).build();
		} catch (final Exception e) {
			return Response.status(Status.UNAUTHORIZED).entity(e.getLocalizedMessage()).build();
		}
	}

	// Helper for authentication
	// TODO: Implement JWT
	private void authenticate(String username, String password) throws AuthenticationException, SQLException {
		final Credential stored = empManager.findByUsername(username).getCredentials();
		if (stored == null) {
			throw new AuthenticationException("Cannot find user with the provided username");
		}
		if (!(password.contentEquals(stored.getPassword()))) {
			throw new AuthenticationException("Invalid password ! Please try again");
		}
	}
}
