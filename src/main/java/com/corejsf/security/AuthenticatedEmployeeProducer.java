package com.corejsf.security;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.Hibernate;

import com.corejsf.access.CredentialManager;
import com.corejsf.model.employee.Credential;
import com.corejsf.model.employee.Employee;
import com.corejsf.security.annotations.AuthenticatedEmployee;

@RequestScoped
public class AuthenticatedEmployeeProducer {

    @Inject
    private CredentialManager credManager;

    @Produces
    @RequestScoped
    @AuthenticatedEmployee
    private Employee authenticatedEmployee;

    // Sets the authenticated employee in the database
    public void handleAuthenticationEvent(@Observes @AuthenticatedEmployee String username) throws SQLException {
        final Credential cred = credManager.find(username);
        Hibernate.initialize(cred.getEmp());
        authenticatedEmployee = cred.getEmp();
    }
}
