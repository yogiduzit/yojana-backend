package com.corejsf.access;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;
import javax.persistence.EntityManager;

import com.corejsf.model.employee.Employee;

@Dependent
@Stateless
public class EmployeeManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

	
	public EmployeeManager() {}
	
	/** find an employee with id. */
	public Employee find(@PathParam("id") String id) {
        return em.find(Employee.class, id);
    }
	
	/** add an employee. */
	public void persist(Employee employee) {
        em.persist(employee);
    }
	
	/** update an employee. */
	public void merge(Employee employee) {
        em.merge(employee);
    }
	
	/** remove an employee. */
	public void remove(Employee employee, String id) {
        employee = find(id);
        em.remove(employee);
    }
	
	/** get all employees. */
	public Employee[] getAll() {       
		TypedQuery<Employee> query = em.createQuery("select e from Employee e",
                Employee.class); 
        java.util.List<Employee> employees = query.getResultList();
        Employee[] suparray = new Employee[employees.size()];
        for (int i=0; i < suparray.length; i++) {
            suparray[i] = employees.get(i);
        }
        return suparray;

    }
	
}
