/**
 * 
 */
package com.yojana.helpers;

import java.util.ArrayList;
import java.util.List;

import com.yojana.model.employee.Employee;
import com.yojana.security.Role;

/**
 * @author yogeshverma
 *
 */
public class RoleHelper {
	public static List<Role> getRolesForEmployee(Employee emp) {
		List<Role> roles = new ArrayList<Role>();
		roles.add(Role.EMPLOYEE);
		if (emp.isAdmin()) {
			roles.add(Role.ADMIN);
		}
		if (emp.isHr()) {
			roles.add(Role.HR);
		}
		if (emp.isProjectManager()) {
			roles.add(Role.PROJECT_MANAGER);
		}
		if (emp.isTimesheetApprover()) {
			roles.add(Role.TIMESHEET_APPROVER);
		}
		return roles;
	}
}
