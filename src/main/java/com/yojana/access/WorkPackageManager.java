package com.yojana.access;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;
import com.yojana.model.timesheet.TimesheetRow;

@Dependent
@Stateless
public class WorkPackageManager implements Serializable {
	
	private final String PREFIX = "WP";
    private final String SEPERATOR = ".";

	/**
	 * 
	 */
	private static final long serialVersionUID = -8709713671106036600L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	
	/** find an employee with id. */
	public WorkPackage find(WorkPackagePK key) {
        return em.find(WorkPackage.class, key);
    }
	
	/** add an employee. */
	@Transactional
	public void persist(WorkPackage workPackage) {
		String id;
		if (workPackage.getParentWPId() == null || workPackage.getParentWPId().isBlank()) {
        	Set<WorkPackage> topLevelWps = getAllWithHierarchyLevel(workPackage.getWorkPackagePk().getProjectID(),
        			0);
        	id = PREFIX + Integer.toString(topLevelWps.size() + 1);
        } else {
        	Set<WorkPackage> children = getChildWPs(workPackage.getWorkPackagePk().getProjectID(),
        			workPackage.getParentWPId());
        	id = workPackage.getParentWPId() + SEPERATOR + (children.size() + 1);
        }
		
		workPackage.getWorkPackagePk().setId(id);
		if (!(workPackage.getParentWPId() == null || workPackage.getParentWPId().isBlank())) {
			WorkPackage parent = find(new WorkPackagePK(workPackage.getParentWPId(), 
					workPackage.getWorkPackagePk().getProjectID()));
			parent.setAllocatedBudget(parent.getAllocatedBudget() + workPackage.getBudget());
			parent.setAllocatedInitialEstimate(parent.getAllocatedInitialEstimate() +
					 workPackage.getInitialEstimate());
			parent.setLowestLevel(false);
			workPackage.setParentWP(parent);
			workPackage.setHierarchyLevel(parent.getHierarchyLevel() + 1);
			em.merge(parent);
		}
        em.persist(workPackage);
    }
	
	/** update an employee. */
	@Transactional
	public void merge(WorkPackage workPackage) {
		WorkPackage old = find(workPackage.getWorkPackagePk());
		if (workPackage.getParentWPId() != null) {
			WorkPackage parent = find(new WorkPackagePK(workPackage.getParentWPId(), 
					workPackage.getWorkPackagePk().getProjectID()));
			double parentBudgetAllocation = parent.getAllocatedBudget() - old.getBudget() + workPackage.getBudget();
			double parentEstimateAllocation = parent.getAllocatedInitialEstimate() - old.getInitialEstimate() + workPackage.getInitialEstimate();
			parent.setAllocatedBudget(parentBudgetAllocation);
			parent.setAllocatedInitialEstimate(parentEstimateAllocation);
			parent.setLowestLevel(false);
			workPackage.setParentWP(parent);
			em.merge(parent);
		}
        em.merge(workPackage);
    }
	
	/** remove an employee. */
	@Transactional
	public void remove(WorkPackage workPackage, WorkPackagePK key) {
        if (workPackage.getParentWPId() != null) {
			WorkPackage parent = find(new WorkPackagePK(workPackage.getParentWPId(), 
					workPackage.getWorkPackagePk().getProjectID()));
			parent.setAllocatedBudget(parent.getAllocatedBudget() + workPackage.getAllocatedBudget());
			parent.setInitialEstimate(parent.getInitialEstimate() + workPackage.getInitialEstimate());
			if (workPackage.isLowestLevel()) {
				parent.setLowestLevel(true);
			}
			em.merge(parent);
		}
        em.remove(workPackage);
    }
	
	public Set<WorkPackage> getAll(String projectId) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId",
                WorkPackage.class); 
		query.setParameter("projectId", projectId);
        List<WorkPackage> workPackages = query.getResultList();
        return new TreeSet<WorkPackage>(workPackages);
    }
	
	public Set<WorkPackage> getAllWithHierarchyLevel(String projectId, int hierarchyLevel) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId"
				+ " and w.hierarchyLevel = :hierarchyLevel", WorkPackage.class); 
		query.setParameter("projectId", projectId);
		query.setParameter("hierarchyLevel", hierarchyLevel);
        List<WorkPackage> workPackages = query.getResultList();
        return new TreeSet<WorkPackage>(workPackages);
    }
	
	public Set<WorkPackage> getChildWPs(String projectId, String parentId) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId"
				+ " and w.parentWPId = :parentId", WorkPackage.class); 
		query.setParameter("projectId", projectId);
		query.setParameter("parentId", parentId);
        List<WorkPackage> workPackages = query.getResultList();
        return new TreeSet<WorkPackage>(workPackages);
    }
	
	public Set<WorkPackage> getAllForResponsibleEngineer(int empId) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.responsibleEngineerId = :id",
                WorkPackage.class); 
		query.setParameter("id", empId);
        List<WorkPackage> workPackages = query.getResultList();
        return new TreeSet<WorkPackage>(workPackages);
    }
	
	public double getCharge(String projectId, String id) {
		TypedQuery<TimesheetRow> query = em.createQuery("select tr from TimesheetRow tr where tr.workPackageId = :id and tr.projectId = :projectId", TimesheetRow.class);
		query.setParameter("id", id);
		query.setParameter("projectId", projectId);
		
		List<TimesheetRow> rows = query.getResultList();
		
		double charges = 0;
		for (TimesheetRow row: rows) {
			charges += row.getSum();
		}
		return charges;
	}
	
	public Map<LocalDate, Double> getWeeklyCharges(String projectId, String id) {
		TypedQuery<TimesheetRow> query = em.createQuery("select tr from TimesheetRow tr JOIN FETCH tr.timesheet t where tr.workPackageId = :id and tr.projectId = :projectId", TimesheetRow.class);
		query.setParameter("id", id);
		query.setParameter("projectId", projectId);
		
		List<TimesheetRow> rows = query.getResultList();
		
		Map<LocalDate, Double> weeklyCharges = new HashMap<LocalDate, Double>();
		for (TimesheetRow row: rows) {
			LocalDate key = row.getTimesheet().getEndWeek();
			if (weeklyCharges.containsKey(key)) {
				weeklyCharges.put(key, weeklyCharges.get(key) + row.getSum());
			} else {
				weeklyCharges.put(key, (double) row.getSum());
			}
		}
		return weeklyCharges;
	}
}
