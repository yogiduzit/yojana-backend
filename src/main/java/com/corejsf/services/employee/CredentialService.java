package com.corejsf.services.employee;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.corejsf.access.CredentialManager;
import com.corejsf.model.employee.Credential;

@Path("/credential")
public class CredentialService {
    
    @Inject
    private CredentialManager credManager;
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response persist(Credential credentials) {
        try {
            credManager.persist(credentials);
            return Response.noContent().build();
        } catch (final Exception e) {
            return Response.serverError().entity(e).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response merge(Credential credentials) {
        try {
            credManager.merge(credentials);
            return Response.noContent().build();
        } catch (final Exception e) {
            return Response.serverError().entity(e).build();
        }
    }
    
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(Credential credentials) {
        try {
            credManager.remove(credentials);
            return Response.noContent().build();
        } catch (final Exception e) {
            return Response.serverError().entity(e).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{username}")
    public Response findByUsername(@PathParam("username") String username) {
        try {
            return Response.ok(credManager.find(username)).build();
        } catch (final Exception e) {
            return Response.serverError().entity(e).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCredentials() {
        try {
            return Response.ok(credManager.getAll()).build();
        } catch (final Exception e) {
            return Response.serverError().entity(e).build();
        }
    }
}
