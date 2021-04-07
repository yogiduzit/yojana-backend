package com.yojana.services.estimate;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.EstimateManager;
import com.yojana.model.estimate.Estimate;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;
import com.yojana.security.annotations.Secured;

@Path("/estimates")
@Secured
public class EstimateService {
    
    @Inject
    private EstimateManager estimateManager;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response persist(Estimate estimate) {
        APIResponse res = new APIResponse();
        UUID uuid = UUID.randomUUID();
        estimate.setEstimateId(uuid);
        estimateManager.persist(estimate);
        return Response.created(URI.create("/estimates/" + estimate.getEstimateId())).entity(res).build();
    }
    
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response merge(@PathParam("id") UUID id, Estimate estimate) {
        APIResponse res = new APIResponse();
        if (!id.equals(estimate.getEstimateId())) {
            res.getErrors().add(ErrorMessageBuilder.badRequest(
                    "EstimateID in route doesn't match estimate id in body", null));
        }
        final Estimate old = estimateManager.find(id);
        if (old == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundSingle("estimate", id.toString(), null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        estimateManager.merge(estimate);
        return Response.ok().entity(new APIResponse()).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") UUID id) {
        final APIResponse res = new APIResponse();
        estimateManager.remove(id);
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") UUID id) {
        Estimate estimate = estimateManager.find(id);
        APIResponse res = new APIResponse();
        if (estimate == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundSingle(
                    "estimate", ""+id, null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimate", estimate);
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        final APIResponse res = new APIResponse();
        List<Estimate> estimates = estimateManager.getAll();
        if (estimates == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundMultiple("estimate", null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimates", estimates);
        return Response.ok().entity(res).build();
    }
}
