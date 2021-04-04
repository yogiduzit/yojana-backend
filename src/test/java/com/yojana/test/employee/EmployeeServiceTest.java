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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import com.yojana.access.EmployeeManager;
import com.yojana.access.TimesheetManager;
import com.yojana.model.employee.Employee;
import com.yojana.response.APIResponse;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.services.employee.EmployeeService;
import com.yojana.test.Constants;

/**
 * Test cases for EmployeeService.
 * Contains mock environment regards to EmployeeManager.
 * 
 * @author Abeer Haroon
 *
 */
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	private static Client client;
	
	static ObjectMapper mapper;
//	static String location;
//	static Employee testEmployee;
//
//	static EmployeeManager mockEmpManager;
	//static APIResponse res;
	
	//Mocking EmployeeService injected members 
	@Mock EmployeeManager empManagerMock;
	@Mock TimesheetManager timesheetManagerMock;
	//--
	@Mock 
	@AuthenticatedEmployee
	Employee employeeMock;
	
	//injecting the mocks into this mock EmployeeService
	@InjectMocks
	static EmployeeService empService = Mockito.mock(EmployeeService.class);

    private static AutoCloseable closeThis;
	
//	@BeforeAll
//	public static void initClient() {
//		client = ClientBuilder.newClient();
//		client.register(JacksonJaxbJsonProvider.class);
//		mapper = new ObjectMapper();
//	}
	
	@BeforeAll
	static public void setUp() {
	    MockitoAnnotations.openMocks(empService);
	    
	    client = ClientBuilder.newClient();
        client.register(JacksonJaxbJsonProvider.class);
	    mapper = new ObjectMapper();
	}


//	@AfterAll
//	public static void closeClient() {
//		client.close();
//	}
	
	/**Test case for GET all employees. 
	 * getAll() in EmployeeService. */
	@Test
	@Order(1)
	public void getAllTest() throws Exception {
	    APIResponse res = new APIResponse();
	    Response serviceResponse = empService.getAll();
	    System.out.println(serviceResponse);
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
}
