// /**
//  *
//  */
// package com.yojana.test;

// import java.util.ArrayList;
// import java.util.List;

// import javax.ws.rs.client.Client;
// import javax.ws.rs.client.ClientBuilder;
// import javax.ws.rs.client.Entity;
// import javax.ws.rs.core.GenericType;
// import javax.ws.rs.core.Response;

// import org.junit.AfterClass;
// import org.junit.BeforeClass;
// import org.junit.Test;

// import com.yojana.domain.Supplier;

// /**
//  * @author yogeshverma
//  *
//  */
// public class SupplierResourceTest {

//     private static Client client;
//     private static String API_URL = "http://localhost:8080/yvermalab09/api/suppliers/";

//     @BeforeClass
//     public static void initClient() {
//         client = ClientBuilder.newClient();
//     }

//     @AfterClass
//     public static void closeClient() {
//         client.close();
//     }

//     @Test
//     public void testSupplierResource() throws Exception {
//         System.out.println("*** Create a new Supplier ***");
//         final Supplier newSupplier = new Supplier();
//         newSupplier.setName("Bill Burke");
//         newSupplier.setContactName("Bill B. Burke");
//         newSupplier.setContactTitle("The JBoss");
//         newSupplier.setContactTitle("bill@jboss.com");
//         newSupplier.setFaxNumber("444-555-6666");
//         newSupplier.setAddress("256 Clarendon Street");
//         newSupplier.setCity("Boston");
//         newSupplier.setStateOrProvince("MA");
//         newSupplier.setPostalCode("02115");
//         newSupplier.setCountry("USA");
//         newSupplier.setNotes("Some notes");
//         newSupplier.setPaymentTerms("30 days");
//         newSupplier.setPhoneNumber("192-168-0114");

//         Response response = client.target(API_URL).request()
//                 .post(Entity.xml(newSupplier));
//         if (response.getStatus() != 201) {
//             throw new RuntimeException("Failed to create");
//         }
//         final String location = response.getLocation().toString();
//         System.out.println("Location: " + location);
//         response.close();

//         System.out.println("*** GET Created Supplier **");
//         Supplier supplier = client.target(location).request().get(Supplier.class);
//         System.out.println(supplier);

//         supplier.setName("William Burke");
//         response = client.target(location).request().put(Entity.xml(supplier));
//         if (response.getStatus() != 204) {
//             throw new RuntimeException("Failed to update");
//         }

//         // Show the update
//         System.out.println("**** After Update ***");
//         supplier = client.target(location).request().get(Supplier.class);
//         System.out.println(supplier);
        
//         System.out.println("*** Delete a Supplier ***");
//         client.target(location).request().delete(Supplier.class);
        
//         System.out.println("*** Get all suppliers ***");
//         ArrayList<Supplier> suppliers = client.target(API_URL).request().get().readEntity(new GenericType<ArrayList<Supplier>>() {});
//         System.out.println(suppliers);
//     }
// }
