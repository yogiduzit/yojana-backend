package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.enterprise.context.ConversationScoped;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.corejsf.model.employee.Employee;

@ConversationScoped
@Path("/employees")
public class EmployeeManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(mappedName = "java:jboss/datasources/MySQLDS")
    private DataSource dataSource;
	
	/** find an employee with id. */
	@GET
    @Path("{id}")
    @Produces("application/xml")
	public Employee find(@PathParam("id") int id) {
        try (Connection connection = dataSource.getConnection(); 
             Statement stmt = connection.createStatement()) {
            ResultSet result = stmt.executeQuery("SELECT * FROM Employee " 
                   + "where EmpID = '" + id + "'");
            if (result.next()) {
                return new Employee(result.getInt("EmpID"),
                        result.getString("EmpName"), result.getNString("EmpUserName"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("Error in find " + id);
            ex.printStackTrace();
            return null;
        }
    }
	
	/** add an employee. */
	@POST
    @Consumes("application/xml")
	public void persist(Employee employee) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO Employee VALUES (?, ?, ?)")){
        	stmt.setInt(1, employee.getId());
            stmt.setString(2, employee.getFullName());
            stmt.setString(3, employee.getUsername());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in persist " + employee);
            ex.printStackTrace();
        }
    }
	
	/** update an employee. */
	@PUT
    @Path("update-employee")
    @Consumes("application/xml")
	public void merge(Employee employee) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                            "UPDATE Employee SET EmpName = ?, EmpUserName = ? "
                                    + "WHERE EmpID =  ?")){
        	stmt.setString(1, employee.getFullName());
        	stmt.setString(2, employee.getUsername());
        	stmt.setInt(3, employee.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in merge " + employee);
            ex.printStackTrace();
        }
    }
	
	/** remove an employee. */
	@DELETE
    @Consumes("application/xml")
	public void remove(Employee employee) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(
                            "DELETE FROM Employee WHERE EmpID =  ?")){
                    stmt.setInt(1, employee.getId());
                    stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error in remove " + employee);
            ex.printStackTrace();
        }
    }
	
	/** get all employees. */
	@GET
    @Path("all")
    @Produces("application/xml")
	public Employee[] getAll() {       
        ArrayList<Employee> employees = new ArrayList<Employee>();
        try (Connection connection = dataSource.getConnection(); 
                Statement stmt = connection.createStatement()) {
            ResultSet result = stmt.executeQuery(
                    "SELECT * FROM Employee ORDER BY EmpID");
            while (result.next()) {
            	employees.add(new Employee(result.getInt("EmpID"),
                		result.getString("EmpName"), 
                        result.getString("EmpUserName")));
            }
        } catch (SQLException ex) {
            System.out.println("Error in getAll");
            ex.printStackTrace();
            return null;
        }

        Employee[] catarray = new Employee[employees.size()];
        return employees.toArray(catarray);
    }
	
}
