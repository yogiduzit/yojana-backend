/**
 *
 */
package com.yojana.test.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yojana.model.employee.Employee;
import com.yojana.model.employee.PayGrade;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
 * @author Daniel Jin
 *
 */
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeTest {

	private static Client client;
	
	static ObjectMapper mapper;
	static String location;
	static Employee testEmployee;

	@BeforeAll
	public static void initClient() {
		client = ClientBuilder.newClient();
		client.register(JacksonJaxbJsonProvider.class);
		mapper = new ObjectMapper();

	}

	@AfterAll
	public static void closeClient() {
		client.close();
	}

//	/**GET call on all Employees. */
	@Test
	@Order(1)
	public void testGetEmployees() throws Exception {
	    APIResponse responseAPI = client.target(Constants.EMPLOYEE_URL)
                .request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .get(APIResponse.class);
	    	    
	    //List<Employee> from api
	    List<Employee> employees = mapper.convertValue(responseAPI.getData().get("employees"), new TypeReference<List<Employee>>() { }); 
	    System.out.println("GET all Employees:");
	    for(int i = 0; i < employees.size(); i++) { //terminal output
	        System.out.println((i+1)+ ". Full Name: " + employees.get(i).getFullName() + "\tID: " 
	                + employees.get(i).getId());
	    }	    	    
	}
	
	/**POST an Employee, then makes GET call on said Employee. 
	 * This test keeps adding an employee so 
	 * be wary of how often this test is called.
	 */
	@Test
	@Order(2)
	public void testAddEmployee() throws Exception {
	    Employee employee = new Employee(); //local
        employee.setFullName("Bloo Waqqle");

        System.out.println("*** Create Employee ***");
        Response response = client.target(Constants.EMPLOYEE_URL)
                .request() 
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .post(Entity.json(employee));
  
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        location = response.getLocation().toString();//location is static
        System.out.println("Location from testAddEmployee(): \n" + "\t" + location);      
        
        response.close();
      
        System.out.println("*** GET Created Employee **");
        APIResponse newEmpResponse = client.target(location) //EMPLOYEE_URL/empID
                .request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .get(APIResponse.class);
        
//      employee static to class
        testEmployee = mapper.convertValue(newEmpResponse.getData().get("employee"), new TypeReference<Employee>() { });
        System.out.println(testEmployee.toString());
        assertEquals(employee.getFullName(), testEmployee.getFullName());
        //assertEquals(employee.getId(), testEmployee.getId());
	} //we now have static testAmployee and static location
//	
	/**PATCH on an Employee. (Partial modification)*/
	@Test
	@Order(3)
	public void testEditEmployee() throws Exception {
	    testEmployee.setFullName("Cheesy McTriple");
        System.out.println("Location: " + Constants.EMPLOYEE_URL + "/" + testEmployee.getId());
        Response response = client.target(Constants.EMPLOYEE_URL + "/" + testEmployee.getId())
                .request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .method("PATCH",Entity.json(testEmployee));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        System.out.println(testEmployee);
        System.out.println("**** After Update ***");
        APIResponse getEmpResponse = client.target(location)
                .request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .get(APIResponse.class);
        //edited employee
        Employee employee = mapper.convertValue(getEmpResponse.getData().get("employee"), new TypeReference<Employee>() { });
        assertEquals(employee.getFullName(), testEmployee.getFullName());

	}
//	
	/**DELETE an Employee. 
//	 * Speculation being: whoever invoking this is authorized.
//	 * this method deleting the static employee.
//	 */
	@Test
	@Order(4)
	public void testDeleteEmployee() throws Exception {
	    System.out.println("*** Delete an Employee ***");
        System.out.println(location);
        
        client.target(location)
        .request()
        .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        .delete();

        assertThrows(NotFoundException.class, () -> {
            client.target(location)
            .request()
            .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
            .get(APIResponse.class);
        });
	}


	
	//utility
	//1. to clean up any unnecessary employees by hand. use String hardCode
//	@Test
//	@Order(1)
//	public void removeEmployee() throws Exception {
//	    
//	    String hardCode = ""; //the employee ID
//	    Employee testEmployee; 
//	    
//	    APIResponse response = client.target(Constants.EMPLOYEE_URL + "/" + hardCode)
//	            .request()
//	            .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//	            .get(APIResponse.class);
//	    
//	    testEmployee = mapper.convertValue(response.getData().get("employee"), new TypeReference<Employee>() { });
//	    System.out.println("Getting:");
//	    System.out.println(testEmployee.getFullName() + "; id: " + testEmployee.getId());
//	    //response.close(); 
//	    //location = response.getLocation().toString();
//        System.out.println("Location: " + Constants.EMPLOYEE_URL + "/" + hardCode);
//        
//        String locate = Constants.EMPLOYEE_URL + "/" + hardCode;
//        System.out.println("Removing Employee:\n" + "Check test result");	    
//        client.target(locate)
//        .request()
//        .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//        .delete();
//
//        assertThrows(NotFoundException.class, () -> {
//            client.target(locate).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
//        });  
//	}
}
