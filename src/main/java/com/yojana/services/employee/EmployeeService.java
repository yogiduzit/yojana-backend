package com.yojana.services.employee;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.EmployeeManager;
import com.yojana.access.PayGradeManager;
import com.yojana.access.TimesheetManager;
import com.yojana.model.employee.Employee;
import com.yojana.model.employee.PayGrade;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

@Secured
@Path("/employees")
public class EmployeeService {

    @Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;
    
    @Inject
    // Provides access to the employee table
    private EmployeeManager employeeManager;
    
    @Inject
    private TimesheetManager timesheetManager;
    
    @Inject
    private PayGradeManager payGradeManager;
    
    @GET
    @Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Finds an employee
	public Response find(@PathParam("id") int id) {
        APIResponse res = new APIResponse();
        if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		Employee employee = employeeManager.find(id);
		if (employee == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("employee",
					id + "",
					null
					));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
        res.getData().put("employee", employee);
        return Response.ok().entity(res).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Inserts an employee in the database
	public Response persist(Employee employee) {
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		PayGrade payGrade = payGradeManager.find(employee.getLabourGradeId());
		employee.setLabourGrade(payGrade);
		Employee currUser = employeeManager.find(authEmployee.getId());
		employee.setCreatorId(currUser.getId());
        employee.setCreator(currUser);
        Employee manager = employeeManager.find(employee.getManagerId());
        employee.setManager(manager);
        Employee timeSheetApprover = employeeManager.find(employee.getTimesheetApproverId());
        employee.setTimesheetApprover(timeSheetApprover);
		employeeManager.persist(employee);
		res.getData().put("id", employee.getId());
		return Response
				.created(URI.create("/employees/" + employee.getId()))
				.entity(res)
				.build();
	}
	

	@PATCH
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// Updates an existing employee
	public Response merge(Employee employee, @PathParam("id") int empId) {
	    APIResponse res = new APIResponse();
	    System.out.println(employee);
	    if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && authEmployee.getId() != empId && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		final Employee emp = employeeManager.find(empId);
		emp.setFullName(employee.getFullName());
        emp.setLabourGrade(payGradeManager.find(employee.getLabourGradeId()));
        emp.setLabourGradeId(employee.getLabourGradeId());
        emp.setManagerId(employee.getManagerId());
        emp.setManager(employeeManager.find(employee.getManagerId()));
        emp.setTimesheetApproverId(employee.getTimesheetApproverId());
        emp.setTimesheetApprover(employeeManager.find(employee.getTimesheetApproverId()));
        emp.setHr(employee.isHr());
        emp.setAdmin(employee.isAdmin());
        emp.setProjectManager(employee.isProjectManager());
		employeeManager.merge(emp);
		return Response.ok().entity(new APIResponse()).build();
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// Deletes an existing employee
	public Response remove(@PathParam("id") int empId) {
	    APIResponse res = new APIResponse();
	    if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		final Employee emp = employeeManager.find(empId);
		employeeManager.remove(emp, empId);
		return Response.ok().entity(new APIResponse()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// Gets a list of all employees
	public Response getAll() throws SQLException {
	    APIResponse res = new APIResponse();
        if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		List<Employee> employees = employeeManager.getAll();
		if (employees == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("employee", null));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
        res.getData().put("employees", employees);
        return Response.ok().entity(res).build();
	}
	
	@GET
	@Path("//{id}")
	@Produces("application/json")
	// Gets a list of all timesheets for an id
	public Response getAllTimesheetsForEmployee(@PathParam("id") UUID empId) {
		final APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager() && !authEmployee.isHr()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		List<Timesheet> timesheets = timesheetManager.getAllForEmployee(empId);
		if (timesheets == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("timesheet", null));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		res.getData().put("timesheets", timesheets);
		return Response.ok().entity(res).build();
	}
}
