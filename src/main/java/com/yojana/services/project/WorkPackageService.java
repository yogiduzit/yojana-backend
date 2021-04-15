/**
 * 
 */
package com.yojana.services.project;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.EstimateManager;
import com.yojana.access.PayGradeManager;
import com.yojana.access.ProjectManager;
import com.yojana.access.WorkPackageManager;
import com.yojana.helpers.WorkPackageHelper;
import com.yojana.model.employee.Employee;
import com.yojana.model.employee.PayGrade;
import com.yojana.model.estimate.Estimate;
import com.yojana.model.estimate.EstimateType;
import com.yojana.model.project.Project;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.AuthenticatedEmployee;
import com.yojana.security.annotations.Secured;

/**
 * @author yogeshverma
 *
 */
@Path("/projects")
@Secured
public class WorkPackageService {
	
	@Inject
	private EstimateManager estimateManager;
	
	@Inject
	// Provides access to the project table
	private ProjectManager projectManager;
	
	@Inject
	private WorkPackageManager wpManager;

	@Inject
	private PayGradeManager paygradeManager;
	
	@Inject
    @AuthenticatedEmployee
    // Gets the authenticated employee 
    private Employee authEmployee;

	@POST
	@Path("/{id}/workPackages")
	@Consumes("application/json")
	@Produces("application/json")
	public Response addWP(@PathParam("id") String projectId, WorkPackage wp) {
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		Project project = projectManager.find(projectId);
		wp.setProject(project);
		
		wpManager.persist(wp);
		res.getData().put("id", wp.getWorkPackagePk().getId());
		return Response.created(URI.create("/projects/" + projectId + "/workPackages/" + wp.getWorkPackagePk()
			.getId()))
			.entity(res)
			.build();
	}
	
	@PATCH
	@Path("/{id}/workPackages/{wpId}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response modifyWP(@PathParam("id") String projectId,
			@PathParam("wpId") String wpId,
			WorkPackage wp) {
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		WorkPackage old = wpManager.find(new WorkPackagePK(wpId, projectId));
		if (old == null) {
			res.getErrors().add(ErrorMessageBuilder.notFoundSingle("workPackage",
					wpId,
					null
					));
			return Response.status(Response.Status.NOT_FOUND).entity(res).build();
		}
		WorkPackageHelper.patchWorkPackage(wp, old);
		Project project = projectManager.find(projectId);
		old.setProject(project);
		
		wpManager.merge(old);
		return Response.ok()
			.entity(res)
			.build();
	}
	
	@GET
	@Path("/{id}/workPackages/{wpId}")
	@Produces("application/json")
	public Response getWorkPackage(
			@PathParam("id") String projectId, 
			@PathParam("wpId") String wpId
	) {
		APIResponse res = new APIResponse();
		if(!authEmployee.isAdmin() && !authEmployee.isProjectManager()) {
            return Response.status(Response.Status.FORBIDDEN).entity(res).build();
        }
		WorkPackage wp = wpManager.find(new WorkPackagePK(wpId, projectId));
		res.getData().put("workPackage", wp);
		return Response.ok().entity(res).build();
	}
	
	@GET
    @Path("/{projectId}/workPackages/{wpId}/estimates")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEstimatesForWorkPackage(@PathParam("projectId") String projectId, 
            @PathParam("wpId") String workPackageId,
            @QueryParam("type") EstimateType type) {
        final APIResponse res = new APIResponse();
        List<Estimate> estimates = new ArrayList<Estimate>();
        
        if (type == null) {
        	estimates = estimateManager.getAllForWorkPackage(workPackageId, projectId);
        } else if (type == EstimateType.planned) {
        	estimates = estimateManager.getPlannedEstimateForWorkPackage(workPackageId, projectId);
        }
        if (estimates == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("estimate", null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimates", estimates);
        return Response.ok().entity(res).build();
    }
	
	@GET
    @Path("/{projectId}/workPackages/{wpId}/charge")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChargeForWorkPackage(@PathParam("projectId") String projectId, 
            @PathParam("wpId") String workPackageId) {
        final APIResponse res = new APIResponse();
        
        PayGrade pg = paygradeManager.find(authEmployee.getLabourGradeId());
        double hours = wpManager.getCharge(projectId, workPackageId);
        double charge = hours * pg.getChargeRate();
        
        res.getData().put("hours", hours);
        res.getData().put("charge", charge);
        return Response.ok().entity(res).build();
    }
	
	@GET
    @Path("/{projectId}/workPackages/{wpId}/weeklyCharges")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeeklyChargesForWorkPackage(@PathParam("projectId") String projectId, 
            @PathParam("wpId") String workPackageId) {
        final APIResponse res = new APIResponse();
        
        PayGrade pg = paygradeManager.find(authEmployee.getLabourGradeId());
        Map<LocalDate, Double> weeklyCharges = wpManager.getWeeklyCharges(projectId, workPackageId);
        Map<LocalDate, Map<String, Double>> weeklyChargeMap = new HashMap<>();
        for (LocalDate date: weeklyCharges.keySet()) {
        	weeklyChargeMap.put(date, new HashMap<>());
        	weeklyChargeMap.get(date).put("charges", weeklyCharges.get(date) * pg.getChargeRate());
        	weeklyChargeMap.get(date).put("hours", weeklyCharges.get(date));
        }
        
        res.getData().put("weeklyCharges", weeklyChargeMap);
        return Response.ok().entity(res).build();
    }
}
