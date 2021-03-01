package com.corejsf.services.employee;

import java.net.URI;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.corejsf.access.EmployeeManager;
import com.corejsf.model.employee.Employee;
import com.corejsf.response.APIResponse;
import com.corejsf.security.annotations.Secured;

@Secured
@Path("/employees")
public class EmployeeService {

	@Inject
	// Provides access to the employee table
	private EmployeeManager employeeManager;
	
	@GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Finds an employee
	public Response find(@PathParam("id") UUID id) {
		Employee employee;
		employee = employeeManager.find(id);
		if (employee == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
    	Map<String, Object> data = new HashMap<String, Object>();
        data.put("employee", employee);
        return Response.ok().entity(new APIResponse(data)).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Inserts an employee in the database
	public Response persist(Employee employee) {
		employeeManager.persist(employee);
		return Response
				.created(URI.create("/employees/" + employee.getId()))
				.entity(new APIResponse())
				.build();
	}
	

	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Updates an existing employee
	public Response merge(Employee employee, @PathParam("id") UUID empId) {
		employee.setId(empId);
		employeeManager.merge(employee);
		return Response.ok().entity(new APIResponse()).build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Deletes an existing employee
	public Response remove(Employee employee, @PathParam("id") UUID empId) {
		employeeManager.remove(employee, empId);
		return Response.ok().entity(new APIResponse()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// Gets a list of all employees
	public Response getAll() throws SQLException {
		List<Employee> employees = employeeManager.getAll();
		if (employees == null) {
			throw new WebApplicationException("There are no employees at the moment", Response.Status.NOT_FOUND);
		}
    	Map<String, Object> data = new HashMap<String, Object>();
        data.put("employees", employees);
        return Response.ok().entity(new APIResponse(data)).build();
	}
}
