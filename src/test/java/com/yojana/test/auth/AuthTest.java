/**
 *
 */
package com.yojana.test.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.yojana.model.employee.Credential;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
 * @author Daniel Jin
 *
 */
@TestMethodOrder(OrderAnnotation.class)
public class AuthTest {

	private static Client client;
	
	static ObjectMapper mapper;
	static String token;
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
	public void testAuthenticate() throws Exception {
		Credential cred = new Credential();
		cred.setUsername("bdlink");
		cred.setPassword("bruce");
		Response response = client.target(Constants.AUTH_URL)
				.request() 
				.post(Entity.json(cred));
		APIResponse tokenResponse = response.readEntity(APIResponse.class);
		token = (String) tokenResponse.getData().get("token");
		assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJlbXBsb3llZSI6ImJkbGluayJ9.Ufu3Wyz1W60xwOrLaR3xkVBT1zeod8LoUNZNlKpCE-hZBjQ_P3FCh1s1bZ1bSFb2aryqQPDm9Cz1UHd3-NieXA", tokenResponse.getData().get("token"));
	}
	@Test
    @Order(2)
    public void testToken() throws Exception {
	    Response response = client.target(Constants.PAYGRADE_URL)
            .request()
            .header("Authorization", "Bearer " + token)
            .get();
    
	     assertEquals(200, response.getStatus());

	}
}
