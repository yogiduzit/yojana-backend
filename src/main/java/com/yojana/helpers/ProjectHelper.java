package com.yojana.helpers;

import com.yojana.model.project.Project;

public class ProjectHelper {
	public static Project patchProject(Project current, Project old) {
		if (current.getBudget() != null) {
			old.setBudget(current.getBudget());
		}
		if (current.getInitialEstimate() != null) {
			old.setInitialEstimate(current.getInitialEstimate());
		}
		return old;
	}
}
