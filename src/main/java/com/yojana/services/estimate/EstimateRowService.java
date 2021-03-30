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
    
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response persist(EstimateRow estimateRow) {
        APIResponse res = new APIResponse();
        Estimate estimate = estimateManager.find(estimateRow.getEstimateRowPk().getEstimateId());
        PayGrade payGrade = payGradeManager.find(estimateRow.getEstimateRowPk().getPayGradeId());
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
        estimateRowManager.persist(estimateRow);
        return Response.created(URI.create("/estimateRows/" + estimateRow.getEstimateRowPk().getEstimateId()
        + "/" + estimateRow.getEstimateRowPk().getPayGradeId())).entity(res).build();
    }
    
    @PATCH
    @Produces("application/json")
    @Consumes("application/json")
    public Response merge(EstimateRow estimateRow) {
        APIResponse res = new APIResponse();
        Estimate estimate = estimateManager.find(estimateRow.getEstimateRowPk().getEstimateId());
        PayGrade payGrade = payGradeManager.find(estimateRow.getEstimateRowPk().getPayGradeId());
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
        estimateRowManager.persist(estimateRow);
        return Response.created(URI.create("/estimateRows/" + estimateRow.getEstimateRowPk().getEstimateId()
        + "/" + estimateRow.getEstimateRowPk().getPayGradeId())).entity(res).build();
    }
    
    @DELETE
    @Path("/{estimateId}/payGrades/{payGradeId}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response remove(@PathParam("estimateId") UUID estimateId,
            @PathParam("payGradeId") String payGradeId) {
        APIResponse res = new APIResponse();
        estimateRowManager.remove(new EstimateRowPK(estimateId, payGradeId));
        return Response.ok().entity(res).build();
    }
    
    @GET
    @Path("/{estimateId}/rows")
    @Produces("application/json")
    public Response getAllForEstimate(@PathParam("estimateId") UUID estimateId) {
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
    @Path("/{estimateId}/payGrades/{payGradeId}")
    @Produces("application/json")
    public Response find(@PathParam("estimateId") UUID estimateId,
            @PathParam("payGradeId") String payGradeId) {
        APIResponse res = new APIResponse();
        EstimateRow estimateRow = estimateRowManager.find(new EstimateRowPK(estimateId, payGradeId));
        if (estimateRow == null) {
            res.getErrors().add(
                    ErrorMessageBuilder.notFoundSingle(
                            "estimate row", estimateId.toString() + " " + payGradeId, null));
            return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        }
        res.getData().put("estimateRow", estimateRow);
        return Response.ok().entity(res).build();
    }
}
