package com.yojana.services.employee;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.yojana.access.PayGradeManager;
import com.yojana.model.employee.PayGrade;
import com.yojana.response.APIResponse;
import com.yojana.security.annotations.Secured;

@Secured
@Path("/paygrade")
public class PayGradeService {
    
    @Inject
    private PayGradeManager payGradeManager;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws SQLException {
        List<PayGrade> payGrades = payGradeManager.getAll();
        if (payGrades == null) {
            throw new WebApplicationException("There are no pay grades at the moment", Response.Status.NOT_FOUND);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("paygrades", payGrades);
        return Response.ok().entity(new APIResponse(data)).build();
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") String labourGrade) {
        PayGrade payGrade = payGradeManager.find(labourGrade);
        if (payGrade == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("paygrade", payGrade);
        return Response.ok().entity(new APIResponse(data)).build();
    }
    
    
    
}
