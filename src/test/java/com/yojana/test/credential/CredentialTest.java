package com.yojana.test.credential;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import com.yojana.model.employee.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yojana.model.employee.Credential;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

@TestMethodOrder(OrderAnnotation.class)
public class CredentialTest {

    private static Client client;
    private final static String API_URL = "http://localhost:8080/yojana-backend/api/credentials";

    static ObjectMapper mapper;
    static String location;
    static Credential testCredential;

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

    @Test
    @Order(1)
    public void testAddEmployee() throws Exception {
        Credential credential = new Credential();

        System.out.println("*** Create credential ***");
        Response response = client.target(API_URL)
                .request()
                .get();

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

        location = response.getLocation().toString();
        System.out.println("Location: " + location);

        response.close();

        System.out.println("*** GET Created Credential **");
        APIResponse newEmpResponse = client.target(location)
                .request()
                .get(APIResponse.class);

        testCredential = mapper.convertValue(newEmpResponse.getData().get("credential"), new TypeReference<Credential>() { });
        System.out.println(testCredential.toString());
    }

//  @Test
//  @Order(2)
//  public void testEditEmployee() throws Exception {
//      testEmployee.setFullName("Twouge Mcfargis");
//      System.out.println("Location: " + API_URL);
//      Response response = client.target(API_URL)
//              .request()
//              .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
//              .put(Entity.json(testEmployee));
//
//      assertEquals(200, response.getStatus());
//
//      System.out.println(testEmployee);
//      System.out.println("**** After Update ***");
//      APIResponse getEmpResponse = client.target(location).request().get(APIResponse.class);
//
//      Employee employee = (Employee) getEmpResponse.getData().get("employee");
//
//      assertEquals(employee.getFullName(), testEmployee.getFullName());
//  }
//
//  @Test
//  @Order(3)
//  public void testRemoveEmployee() throws Exception {
//      System.out.println("*** Delete a Supplier ***");
//      System.out.println(location);
//      client.target(location).request().delete();
//
//      assertThrows(NotFoundException.class, () -> {
//          client.target(location).request().get(APIResponse.class);
//      });
//
//  }
}
