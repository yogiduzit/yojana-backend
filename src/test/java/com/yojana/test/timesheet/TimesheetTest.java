 /**
  *
  */
 package com.yojana.test.timesheet;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
 public class TimesheetTest {

     private static Client client;
     
     
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
 		APIResponse newEmpResponse = client.target(Constants.EMPLOYEE_URL + "/" + Constants.TEST_ID)
                .request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                .get(APIResponse.class);
 		testEmployee = mapper.convertValue(newEmpResponse.getData().get("employee"), new TypeReference<Employee>() { });
 		
     }
     
     @Test
     @Order(1)
     public void testAddTimesheet() throws Exception {
         Timesheet timesheet = new Timesheet();
         timesheet.setEmployee(testEmployee);
         timesheet.setOwnerId(testEmployee.getId());
         timesheet.setStatus(TimesheetStatus.pending);
         timesheet.setEndWeek(LocalDate.now()); 
         
         Entity<Timesheet> entity = Entity.json(timesheet);
         
         Response response = client.target(Constants.TIMESHEET_URL)
        		 .request()
        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        		 .post(entity);
         
         location = response.getLocation().toString();
         System.out.println(location);
         response.close();
         
         APIResponse newTimesheetResponse = client.target(location)
        		 .request()
        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        		 .get(APIResponse.class);
         testTimesheet = mapper.convertValue(newTimesheetResponse.getData().get("timesheet"), new TypeReference<Timesheet>() { });
         assertEquals(testTimesheet.getOwnerId(), timesheet.getOwnerId());
     }
     
     @Test
     @Order(2)
     public void testEditTimesheet() throws Exception {
         testTimesheet.setStatus(TimesheetStatus.approved);
         testTimesheet.setOwnerId(testEmployee.getId());
         
         client.target(location)
        		 .request()
        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        		 .build("PATCH", Entity.json(testTimesheet))
        		 .invoke(APIResponse.class);
        		 
         APIResponse getTimesheetResponse = client.target(location)
        		 .request()
        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        		 .get(APIResponse.class);
         Timesheet timesheet = mapper.convertValue(getTimesheetResponse.getData().get("timesheet"), new TypeReference<Timesheet>() { });
         
         System.out.println(testTimesheet.getStatus().toString());
         System.out.println(timesheet.getStatus().toString());
         
         assertEquals(testTimesheet.getStatus().toString(), timesheet.getStatus().toString());
     }
     
     
     
     @Test
     @Order(3)
     public void testAddTimesheetRow() throws Exception {
         TimesheetRow timesheetRow = new TimesheetRow();
         timesheetRow.setTimesheetId(testTimesheet.getId());
         timesheetRow.setIndex(0);
         timesheetRow.setPackedHours(1000);
         timesheetRow.setProjectId("PR123");
         timesheetRow.setWorkPackageId("WP1.1");
         
         String URL = location + "/rows";
         System.out.println(URL);
         Response response = client.target(URL)
                 .request(MediaType.APPLICATION_JSON)
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .post(Entity.json("{\"timesheetId\":\"45700000-0000-0000-0000-000000000000\",\"workPackageId\":\"WP1.1\",\"projectId\":\"PR123\",\"packedHours\":1000}"));
         

         response.close();
         System.out.println(response.getStatusInfo().toString());
         APIResponse newTimesheetResponse = client.target(location + "/rows")
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<TimesheetRow> timesheetRows = mapper.convertValue(newTimesheetResponse.getData().get("timesheetRows"), new TypeReference<List<TimesheetRow>>() { });
         System.out.println(timesheetRows.size());
         assertTrue(timesheetRows.size() > 0);
     }
     
     @Test
     @Order(4)
     public void testRemoveTimesheet() throws Exception {
        client.target(location).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).delete();
       
        assertThrows(NotFoundException.class, () -> {
            client.target(location).request().header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
        });  
     }
 }
