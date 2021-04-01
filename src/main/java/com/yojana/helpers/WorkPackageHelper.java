package com.yojana.helpers;

import com.yojana.model.project.WorkPackage;

public class WorkPackageHelper {
	public static WorkPackage patchWorkPackage(WorkPackage current, WorkPackage old) {
		if (current.getAllocatedBudget() != null) {
			old.setAllocatedBudget(current.getAllocatedBudget());
		}
		if (current.getAllocatedInitialEstimate() != null) {
			old.setAllocatedInitialEstimate(current.getAllocatedInitialEstimate());
		}
		if (current.getBudget() != null) {
			old.setBudget(current.getBudget());
		}
		if (current.getInitialEstimate() != null) {
			old.setInitialEstimate(current.getInitialEstimate());
		}
		return old;
	}
}
