package com.corejsf.services.employee;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.corejsf.access.CredentialManager;
import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Credential;
import com.corejsf.model.employee.Employee;

@Path("/credentials")
public class CredentialService {

	@Inject
	private CredentialManager credManager;
	
//	@Inject
//	private EmployeeManager empManager;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response persist(Credential credentials) {
		try {
			credManager.persist(credentials);
			return Response.noContent().build();
		} catch (final Exception e) {
			return Response.serverError().entity(e).build();
		}
	}

	@PATCH
	@Path("/{empID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response merge(Credential credentials, @PathParam("empID") UUID empId) {
		try {
			//TODO: Need to figure out how to update a credential
//			Do we need both credential and employee id?
//			Employee employee = empManager.find(empId);
//			
//			employee.getCredentials()
//			UUID cred = credManager.find(credId).getEmployee().getId();
//			
//			credentials.setId(credId);
//			
//			Employee employee = new Employee();
//			employee.setId(empId);
//			
//			credentials.setEmployee(employee);
//			credManager.merge(credentials);
			return Response.noContent().build();
		} catch (final Exception e) {
			return Response.serverError().entity(e).build();
		}
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(Credential credentials) {
		try {
			credManager.remove(credentials);
			return Response.noContent().build();
		} catch (final Exception e) {
			return Response.serverError().entity(e).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Credential> getAll() {
		return credManager.getAll();
	}
}
