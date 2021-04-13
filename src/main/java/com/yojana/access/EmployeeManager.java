package com.yojana.access;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.enterprise.context.ConversationScoped;
import javax.persistence.EntityManager;

import com.yojana.model.employee.Employee;
import com.yojana.model.timesheet.TimesheetRow;

@ConversationScoped
public class EmployeeManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	
	/** find an employee with id. */
	public Employee find(int id) {
        return em.find(Employee.class, id);
    }
	
	public Employee findByUsername(String username) {
		TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e JOIN e.credential c"
				+ " WHERE c.username=:username", Employee.class);
		query.setParameter("username", username);
		return query.getSingleResult();
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
	public void remove(Employee employee, int id) {
        employee = find(id);
        em.remove(employee);
    }
	
	public List<Employee> getAll() {       
		TypedQuery<Employee> query = em.createQuery("select e from Employee e",
                Employee.class); 
        List<Employee> employees = query.getResultList();
        return employees;
    }
	
	public double getHoursForWeek(int empId, LocalDate endWeek) {
        TypedQuery<TimesheetRow> query = em.createQuery("select tr from TimesheetRow tr JOIN tr.timesheet t where t.endWeek = :endWeek AND t.ownerId = :ownerId", TimesheetRow.class);
        query.setParameter("ownerId", empId);
        query.setParameter("endWeek", endWeek);
        
        List<TimesheetRow> rows = query.getResultList();
        double sum = 0.0;
        for (TimesheetRow row: rows) {
        	sum += row.getSum();
        }
        return sum;
    }
	
}
