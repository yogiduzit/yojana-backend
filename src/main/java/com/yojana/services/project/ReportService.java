package com.yojana.services.project;

import javax.inject.Inject;
import javax.ws.rs.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yojana.access.ProjectManager;
import com.yojana.model.project.Report;
import com.yojana.security.annotations.Secured;

@Path("/reports")
@Secured
public class ReportService {
	
	@Inject
	private ProjectManager projectManager;
	
	public String toJson(Report report) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(report);
		return json;
	}

}
