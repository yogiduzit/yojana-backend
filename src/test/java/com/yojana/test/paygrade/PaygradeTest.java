 /**
  *
  */
 package com.yojana.test.paygrade;

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
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yojana.model.employee.Employee;
import com.yojana.model.employee.PayGrade;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetStatus;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
  * @author Daniel Jin
  *
  */
@TestMethodOrder(OrderAnnotation.class)
 public class PaygradeTest {

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
         
     }

     @AfterAll
     public static void closeClient() {
         client.close();
     }

     /**GET all PayGrades. */
     @Test
     @Order(1)
     public void testGetPaygrades() throws Exception {
         System.out.println(Constants.PAYGRADE_URL);
         APIResponse response = client.target(Constants.PAYGRADE_URL)
        		 .request()
        		 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
        		 .get(APIResponse.class);
         List<PayGrade> paygrades = mapper.convertValue(response.getData().get("paygrades"), new TypeReference<List<PayGrade>>() { });
         for(int i = 0; i < paygrades.size(); i++) {
             System.out.println((i+1) + ". Labour Grade: " + paygrades.get(i).getLabourGrade());
             System.out.println((i+1) + ". Pay Grade:" + paygrades.get(i).getChargeRate());
             System.out.println(" ----------------------- ");
         }
         assertNotEquals(paygrades.size(), 0); //check if not empty
     }
     
     /**GET PayGrades by LabourGrade. 
      * @param laboutGrade is the Labour Grade. (Primary Key)
      * ParameterizedTest annotation runs test with parameters.
      * 
      * ValueSource is the exact string value itself that is the parameter.
      * In this case, "PS" is a parameter going into the test method
      * To add more parameters follow this format: @ ValueSource(strings = {"", "  "})
      */
       @ParameterizedTest
       @ValueSource(strings = {"PS"} )
       @Order(2)
       public void testGetPaygrade(String labourGrade) throws Exception {
           String location = Constants.PAYGRADE_URL + "/" + labourGrade;
           System.out.println("location: " + location);
           assertDoesNotThrow(() -> {
               APIResponse response = client.target(location)
                       .request()
                       .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                       .get(APIResponse.class);
           });
       }
 }
