 /**
  *
  */
 package com.yojana.test.leaveRequest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
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
import com.yojana.model.employee.LeaveRequest;
import com.yojana.model.employee.PayGrade;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetRow;
import com.yojana.model.timesheet.TimesheetStatus;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
  * @author Daniel Jin
  *
  */
@TestMethodOrder(OrderAnnotation.class)
 public class LeaveRequestTest {

     private static Client client;
     
     
     
     static String location;
     static ObjectMapper mapper;
     static LeaveRequest testLeaveRequest;
     
     @BeforeAll
     public static void initClient() throws Exception {
         client = ClientBuilder.newClient();
         client.register(JacksonJaxbJsonProvider.class);
         mapper = new ObjectMapper();
         
     }

     @AfterAll
     public static void closeClient() {
         client.close();
     }

     
     @Test
     @Order(1)
     public void testAddLeaveRequest() throws Exception {
         Response response = client.target(Constants.LEAVEREQUEST_URL)
                 .request(MediaType.APPLICATION_JSON)
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .post(Entity.json("{\"empId\":2,\"startDate\":1614363200000,\"endDate\":1618739200000,\"type\":\"Medical\",\"description\":\"Summer\"}"));
         response.close();
         location = response.getLocation().toString();
         System.out.println(response.getStatusInfo().toString());
         APIResponse getResponse = client.target(location)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         testLeaveRequest = mapper.convertValue(getResponse.getData().get("leaveRequest"), new TypeReference<LeaveRequest>() { });
         assertTrue(testLeaveRequest.getEmpId() == 2);
     }
     @Test
     @Order(2)
     public void testGetLeaveRequestsById() throws Exception {
         System.out.println(Constants.LEAVEREQUEST_URL + "/31324000-0000-0000-0000-000000000000");
         System.out.println("location: " + location);
         
         assertDoesNotThrow(() -> {
              client.target(Constants.LEAVEREQUEST_URL + "/31324000-0000-0000-0000-000000000000")
                     .request()
                     .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                     .get(APIResponse.class);
         });
     }
     
     @Test
     @Order(3)
     public void testGetLeaveRequestsForEmployee() throws Exception {
         System.out.println(Constants.LEAVEREQUEST_URL + "/emp/1");
         System.out.println("location: " + location);
         APIResponse response = client.target(Constants.LEAVEREQUEST_URL + "/emp/1")
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<LeaveRequest> leaveRequests = mapper.convertValue(response.getData().get("leaveRequests"), new TypeReference<List<LeaveRequest>>() { });
         assertNotEquals(leaveRequests.size(), 0);
     }
     
     @Test
     @Order(4)
     public void testGetLeaveRequestsByType() throws Exception {
         System.out.println(Constants.LEAVEREQUEST_URL + "/type/Medical");
         System.out.println("location: " + location);
         APIResponse response = client.target(Constants.LEAVEREQUEST_URL + "/emp/1")
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<LeaveRequest> leaveRequests = mapper.convertValue(response.getData().get("leaveRequests"), new TypeReference<List<LeaveRequest>>() { });
         assertNotEquals(leaveRequests.size(), 0);
     }
     
     @Test
     @Order(5)
     public void testGetLeaveRequests() throws Exception {
         System.out.println(Constants.LEAVEREQUEST_URL);
         APIResponse response = client.target(Constants.LEAVEREQUEST_URL)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<LeaveRequest> leaveRequests = mapper.convertValue(response.getData().get("leaveRequests"), new TypeReference<List<LeaveRequest>>() { });
         System.out.println(leaveRequests.get(0).getType());
         assertNotEquals(leaveRequests.size(), 0);
     }
     
     @Test
     @Order(6)
     public void testEditLeaveRequest() throws Exception {
         client.target(location)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .build("PATCH", Entity.json("{\"type\":\"Vacation\",\"empId\":1}"))
                 .invoke(APIResponse.class);
                 
         APIResponse getResponse = client.target(location)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         LeaveRequest request = mapper.convertValue(getResponse.getData().get("leaveRequest"), new TypeReference<LeaveRequest>() { });
         
         System.out.println(request.getType().toString());
         
         assertEquals("Vacation", request.getType().toString());
     }
     
     @Test
     @Order(7)
     public void testRemoveLeaveRequest() throws Exception {
         System.out.println(location);
         client.target(location).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).delete();

         assertThrows(NotFoundException.class, () -> {
             client.target(location).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
         });

     }

 }
