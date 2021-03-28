 /**
  *
  */
 package com.yojana.test.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
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
import com.yojana.model.project.Project;
import com.yojana.model.project.ProjectStatus;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.model.project.WorkPackageStatus;
import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.timesheet.TimesheetStatus;
import com.yojana.response.APIResponse;
import com.yojana.test.Constants;

/**
  * Encompasses test cases for both projects and work packages.
  * @author Daniel Jin
  *
  */
@TestMethodOrder(OrderAnnotation.class)
 public class ProjectWPTest {

     private static Client client;
     
     
     static String projectLocation;
     static String wpLocation;
     static ObjectMapper mapper;
     static Employee testEmployee;
     static Project testProject;
     static WorkPackage testWP;
     
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
     
     /**
      * Encompasses both persist and get specific project for project API.
      * @throws Exception
      */
     @Test
     @Order(1)
     public void testAddProject() throws Exception {
         Project project = new Project();
         int temp = (int)(Math.random() * 1000000000);
         project.setId("" + temp);
         project.setStatus(ProjectStatus.pending);

         Response response = client.target(Constants.PROJECT_URL)
                 .request() 
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .post(Entity.json(project));

         projectLocation = response.getLocation().toString();
         System.out.println(projectLocation);
         response.close();
         
         APIResponse newProjectResponse = client.target(projectLocation)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         testProject = mapper.convertValue(newProjectResponse.getData().get("project"), new TypeReference<Project>() { });
         
         assertEquals(testProject.getId(), project.getId());
     }
     
     @Test
     @Order(2)
     public void testGetProjects() throws Exception {
         System.out.println(Constants.PAYGRADE_URL);
         APIResponse response = client.target(Constants.PROJECT_URL)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<Project> projects = mapper.convertValue(response.getData().get("projects"), new TypeReference<List<Project>>() { });
         assertNotEquals(projects.size(), 0);
     }

     @Test
     @Order(3)
     public void testAddWorkPackage() throws Exception {
         WorkPackage workPackage = new WorkPackage();
         WorkPackagePK PK = new WorkPackagePK();
         int temp = (int)(Math.random() * 1000000000);
         PK.setId("" + temp);
         PK.setProjectID(testProject.getId());
         
         workPackage.setWorkPackagePk(PK);
         workPackage.setStatus(WorkPackageStatus.open);
         System.out.println(projectLocation + "/workPackages");
         Response response = client.target(projectLocation + "/workPackages")
                 .request() 
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .post(Entity.json(workPackage));
         
         wpLocation = response.getLocation().toString();
         System.out.println(projectLocation);
         response.close();
         
         APIResponse newProjectResponse = client.target(wpLocation)
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         testWP = mapper.convertValue(newProjectResponse.getData().get("workPackage"), new TypeReference<WorkPackage>() { });
         
         assertEquals(testWP.getWorkPackagePk().getId(), PK.getId());
     }
     
     @Test
     @Order(4)
     public void testGetWorkPackages() throws Exception {
         System.out.println(Constants.PAYGRADE_URL);
         APIResponse response = client.target(projectLocation + "/workPackages")
                 .request()
                 .header("Authorization", "Bearer " + Constants.API_TEST_KEY)
                 .get(APIResponse.class);
         List<WorkPackage> wps = mapper.convertValue(response.getData().get("workPackages"), new TypeReference<List<WorkPackage>>() { });
         assertNotEquals(wps.size(), 0);
     }
 }
