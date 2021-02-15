package com.corejsf.security;

import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.security.annotations.AuthenticatedEmployee;

@RequestScoped
public class AuthenticatedEmployeeProducer {

    @Inject
    private EmployeeManager empManager;

    @Produces
    @RequestScoped
    @AuthenticatedEmployee
    private Employee authenticatedEmployee;

    // Sets the authenticated employee in the database
    public void handleAuthenticationEvent(@Observes @AuthenticatedEmployee String username) throws SQLException {
        authenticatedEmployee = empManager.findByUsername(username);
    }
}
