 /**
  *
  */
 package com.yojana.test.timesheet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.UUID;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yojana.model.employee.Employee;
import com.yojana.model.timesheet.Status;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
  * @author Daniel Jin
  *
  */
@TestMethodOrder(OrderAnnotation.class)
 public class TimesheetTest {

     private static Client client;
     private static String API_URL = "http://localhost:8080/comp4911-pms-backend/api";
     
     private static String TIMESHEET_URL = API_URL + "/timesheets";
     private static String EMPLOYEE_URL = API_URL + "/employees";
     
     static String location;
     static ObjectMapper mapper;
     static Employee testEmployee;
     static Timesheet testTimesheet;
     
     @BeforeAll
     public static void initClient() throws Exception {
         client = ClientBuilder.newClient();
         client.register(JacksonJaxbJsonProvider.class);
         mapper = new ObjectMapper();
         
         setupEmployee();
     }

     @AfterAll
     public static void closeClient() {
         client.close();
     }
     
     public static void setupEmployee() throws Exception {
 		System.out.println("*** GET Created Employee **");
 		testEmployee = new Employee();
 		testEmployee.setId(UUID.fromString("31000000-0000-0000-0000-000000000000"));
     }
     
//     @Test
//     @Order(1)
//     public void testAddTimesheet() throws Exception {
//         Timesheet timesheet = new Timesheet();
//         timesheet.setEmployee(testEmployee);
//         timesheet.setStatus(Status.pending);
//         timesheet.setEndWeek(LocalDate.now());
//         
//         Entity<Timesheet> entity = Entity.json(timesheet);
//         
//         Response response = client.target(TIMESHEET_URL)
//        		 .request()
//        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//        		 .post(entity);
//         
//         location = response.getLocation().toString();
//         System.out.println(location);
//         response.close();
//         
//         APIResponse newTimesheetResponse = client.target(location)
//        		 .request()
//        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//        		 .get(APIResponse.class);
//         
//         testTimesheet = mapper.convertValue(newTimesheetResponse.getData().get("timesheet"), new TypeReference<Timesheet>() { });
//         
//         assertEquals(testTimesheet.getOwnerId(), timesheet.getOwnerId());
//     }
//     
//     @Test
//     @Order(3)
//     public void testEditTimesheet() throws Exception {
//         testTimesheet.setStatus(Status.approved);
//         Response response = client.target(location).request().put(Entity.json(testTimesheet));
//         System.out.println(response.getStatus());
//         Request request = client.target(location).request().get(Request.class);
//         Timesheet timesheet = request.data.timesheet[0];
//         
//         System.out.println(testTimesheet.getStatus().toString());
//         System.out.println(timesheet.getStatus().toString());
//         
//         assertEquals(testTimesheet.getStatus().toString(), timesheet.getStatus().toString());
//     }
//     
//     @Test
//     @Order(4)
//     public void testRemoveEmployee() throws Exception {
//        client.target(location).request().delete();
//
//        assertThrows(NotFoundException.class, () -> {
//            Request request = client.target(location).request().get(Request.class);
//        });  
//         
//     }
 }
