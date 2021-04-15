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
import javax.ws.rs.core.Response;

import com.yojana.access.EstimateManager;
import com.yojana.access.EstimateRowManager;
import com.yojana.access.PayGradeManager;
import com.yojana.model.employee.PayGrade;
import com.yojana.model.estimate.Estimate;
import com.yojana.model.estimate.EstimateRow;
import com.yojana.model.estimate.EstimateRowPK;
import com.yojana.response.APIResponse;
import com.yojana.response.errors.ErrorMessageBuilder;

@Path("/estimates")
public class EstimateRowService {
    
    @Inject
    private EstimateManager estimateManager;
    
    @Inject
    private PayGradeManager payGradeManager;

    @Inject
    private EstimateRowManager estimateRowManager;
    
    @Path("/{id}/rows")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response persist(@PathParam("id") UUID id, EstimateRow estimateRow) {
        APIResponse res = new APIResponse();
        Estimate estimate = estimateManager.find(id);
        PayGrade payGrade = payGradeManager.find(estimateRow.getPaygradeId());
        if (estimate == null) {
            ErrorMessageBuilder.notFound("estimate", null);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        if (payGrade == null) {
            ErrorMessageBuilder.notFound("pay grade", null);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        estimateRow.setEstimate(estimate);
        estimateRow.setPayGrade(payGrade);
        estimateRow.getEstimateRowPk().setEstimateId(id);
        estimateRowManager.persist(estimateRow);
        res.getData().put("id", estimateRow.getEstimateRowPk());
        return Response.created(URI.create("/estimateRows/" + estimateRow.getEstimateRowPk().getEstimateId()
        + "/" + estimateRow.getEstimateRowPk().getIndex())).entity(res).build();
    }
    
    @PATCH
    @Path("/{id}/rows/{index}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response merge(@PathParam("id") UUID id, @PathParam("index") int index,
    		EstimateRow estimateRow) {
        APIResponse res = new APIResponse();
        Estimate estimate = estimateManager.find(id);
        PayGrade payGrade = payGradeManager.find(estimateRow.getPaygradeId());
        if (estimate == null) {
            ErrorMessageBuilder.notFound("estimate", null);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        if (payGrade == null) {
            ErrorMessageBuilder.notFound("pay grade", null);
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        estimateRow.setEstimate(estimate);
        estimateRow.setPayGrade(payGrade);
        estimateRowManager.merge(estimateRow);
        return Response.created(URI.create("/estimateRows/" + estimateRow.getEstimateRowPk().getEstimateId()
        + "/" + estimateRow.getEstimateRowPk().getIndex())).entity(res).build();
    }
    
    @DELETE
    @Path("/{id}/rows/{index}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response remove(@PathParam("id") UUID estimateId,
            @PathParam("index") int index) {
        APIResponse res = new APIResponse();
        estimateRowManager.remove(new EstimateRowPK(estimateId, index));
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/{id}/rows")
    @Produces("application/json")
    public Response getAllForEstimate(@PathParam("id") UUID estimateId) {
        List<EstimateRow> estimateRows = estimateRowManager.getAllForEstimate(estimateId);
        APIResponse res = new APIResponse();
        if (estimateRows == null) {
            res.getErrors().add(ErrorMessageBuilder.notFoundSingle("estimate", estimateId.toString(), null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimateRows", estimateRows);
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/{id}/rows/{index}")
    @Produces("application/json")
    public Response find(@PathParam("id") UUID estimateId,
            @PathParam("index") int index) {
        APIResponse res = new APIResponse();
        EstimateRow estimateRow = estimateRowManager.find(new EstimateRowPK(estimateId, index));
        if (estimateRow == null) {
            res.getErrors().add(
                    ErrorMessageBuilder.notFoundSingle(
                            "estimate row", estimateId.toString() + " " + index, null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimateRow", estimateRow);
        return Response.ok().entity(res).build();
    }
}
