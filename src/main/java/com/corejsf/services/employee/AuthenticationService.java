package com.corejsf.services.employee;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
//
//import javax.inject.Inject;
//import javax.naming.AuthenticationException;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Hex;

import com.corejsf.access.CredentialManager;
import com.corejsf.helpers.PasswordHelper;
import com.corejsf.model.employee.Credential;

@Path("/authentication")
public class AuthenticationService {

    @Inject
    // Provides access to the Credentials table
    private CredentialManager credManager;

    // Helper for password encryption
    private PasswordHelper passwordHelper;

    // Provides authentication support
    public AuthenticationService() {
        try {
            passwordHelper = new PasswordHelper();
        } catch (final NoSuchAlgorithmException e) {
            passwordHelper = null;
            e.printStackTrace();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    // Authenticates a user
    public Response authenticateUser(Credential credentials) {
        try {
            authenticate(credentials.getUsername(), credentials.getPassword());
            
            final String token = Hex
                    .encodeHexString(passwordHelper.encrypt(credentials.getUsername() + credentials.getPassword()));
            return Response.ok(token).build();
        } catch (final Exception e) {
            return Response.status(Status.UNAUTHORIZED).entity(e.getLocalizedMessage()).build();
        }
    }

    // Helper for authentication
    // TODO: Implement JWT
    private void authenticate(String username, String password) throws AuthenticationException, SQLException {
        final Credential stored = credManager.find(username);
        if (stored == null) {
            throw new AuthenticationException("Cannot find user with the provided username");
        }
        if (!(password.contentEquals(stored.getPassword()))) {
            throw new AuthenticationException("Invalid password ! Please try again");
        }
    }
}
