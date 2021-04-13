package com.yojana.model.projectemployee;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.yojana.model.employee.Employee;
import com.yojana.model.project.Project;

@Entity
@Table(name = "ProjectEmployee")
public class ProjectEmployee  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7569341008564405379L;
	
	/**
     * Represents the ID of the employee
     */
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EmpID")
    private Employee employee;
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ProjectID")
    private Project project;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	
	

	

}