package com.corejsf.access;

import java.io.Serializable;
import java.util.List;

import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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
	
	/** find an employee with id. */
	public Employee find(UUID id) {
        return em.find(Employee.class, id);
    }
	
	/** add an employee. */
	@Transactional
	public void persist(Employee employee) {
        em.persist(employee);
    }
	
	/** update an employee. */
	@Transactional
	public void merge(Employee employee) {
        em.merge(employee);
    }
	
	/** remove an employee. */
	@Transactional
	public void remove(Employee employee, UUID id) {
        employee = find(id);
        em.remove(employee);
    }
	
	public List<Employee> getAll() {       
		TypedQuery<Employee> query = em.createQuery("select e from Employee e",
                Employee.class); 
        List<Employee> employees = query.getResultList();
        return employees;
    }
	
}
