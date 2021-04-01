package com.yojana.services.validators;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import com.yojana.model.project.WorkPackage;
import com.yojana.response.errors.ErrorMessage;

public class WorkPackageValidator {
	
	public static List<ErrorMessage> validatePersist(WorkPackage wp) {
		List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		if (wp.getAllocatedBudget() > wp.getBudget()) {
			errors.add(new ErrorMessage(Status.BAD_REQUEST.getStatusCode(), "Allocated budget exceeds budget", null));
		}
		if (wp.getAllocatedInitialEstimate() > wp.getInitialEstimate()) {
			errors.add(new ErrorMessage(Status.BAD_REQUEST.getStatusCode(), "Allocated initial estimate exceeds initial estimate", null));
		}
		
		return errors;
	}
}
