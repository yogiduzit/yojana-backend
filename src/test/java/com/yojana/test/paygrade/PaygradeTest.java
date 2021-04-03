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

    @Test
    @Order(1)
    public void testGetPaygrades() throws Exception {
        System.out.println(Constants.PAYGRADE_URL);
        APIResponse response = client.target(Constants.PAYGRADE_URL).request()
                .header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
        List<PayGrade> paygrades = mapper.convertValue(response.getData().get("paygrades"),
                new TypeReference<List<PayGrade>>() {
                });
        System.out.println(paygrades.get(0).getChargeRate());
        assertNotEquals(paygrades.size(), 0);
    }

    @Test
    @Order(2)
    public void testGetPaygrade() throws Exception {
        String location = Constants.PAYGRADE_URL + "/PS";
        System.out.println("location: " + location);
        assertDoesNotThrow(() -> {
            APIResponse response = client.target(location).request()
                    .header("Authorization", "Bearer " + Constants.API_TEST_KEY).get(APIResponse.class);
        });
    }

}
