/**
 *
 */
package com.yojana.test.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

//	@Test
//	@Order(1)
//	public void testAddEmployee() throws Exception {
//		Employee employee = new Employee();
//		employee.setFullName("Douge Mcfargis");
//
//		System.out.println("*** Create Employee ***");
//		Response response = client.target(Constants.EMPLOYEE_URL)
//				.request() 
//				.header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//				.post(Entity.json(employee));
//
//		assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
//
//		location = response.getLocation().toString();
//		System.out.println("Location: " + location);
//		 
//		response.close();
//		
//		System.out.println("*** GET Created Employee **");
//		APIResponse newEmpResponse = client.target(location)
//				.request()
//				.header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//				.get(APIResponse.class);
//		
//		testEmployee = mapper.convertValue(newEmpResponse.getData().get("employee"), new TypeReference<Employee>() { });
//		System.out.println(testEmployee.toString());
//		assertEquals(employee.getFullName(), testEmployee.getFullName());
//	}
//
//	@Test
//	@Order(2)
//	public void testEditEmployee() throws Exception {
//		testEmployee.setFullName("Twouge Mcfargis");
//		System.out.println("Location: " + Constants.EMPLOYEE_URL + "/" + testEmployee.getId());
//		client.target(Constants.EMPLOYEE_URL + "/" + testEmployee.getId())
//				.request()
//				.header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//				.method("PATCH",Entity.json(testEmployee));
//
//
//		System.out.println(testEmployee);
//		System.out.println("**** After Update ***");
//		APIResponse getEmpResponse = client.target(location).request().get(APIResponse.class);
//
//		Employee employee = mapper.convertValue(getEmpResponse.getData().get("employee"), new TypeReference<Employee>() { });
//		assertEquals(employee.getFullName(), testEmployee.getFullName());
//	}

//	@Test
//	@Order(1)
//	public void testRemoveEmployee() throws Exception {
//		System.out.println("*** Delete a Supplier ***");
//		System.out.println(location);
//		client.target(location).request().delete();
//
//		assertThrows(NotFoundException.class, () -> {
//			client.target(location).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
//		});
//
//	}
	
	//utility
//	1. to clean up any unnecessary employees by hand. use String hardCode
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
//	    response.close(); 
//	    location = response.getLocation().toString();
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
