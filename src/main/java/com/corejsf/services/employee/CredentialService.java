package com.corejsf.services.employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.corejsf.access.CredentialManager;
import com.corejsf.model.employee.Credential;
import com.corejsf.response.APIResponse;

@Path("/credentials")
public class CredentialService {

	@Inject
	private CredentialManager credManager;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public Response persist(Credential credentials) {
		credManager.persist(credentials);
		return Response.ok().entity(new APIResponse()).build();
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response remove(Credential credentials, @PathParam("id") UUID credID) {
		credentials.setId(credID);
		credManager.remove(credentials);
		return Response.ok().entity(new APIResponse()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response getAll() {
		final List<Credential> credentials = credManager.getAll();
    	Map<String, Object> data = new HashMap<String, Object>();
        data.put("credentials", credentials);
        return Response.ok().entity(new APIResponse(data)).build();
	}
}
