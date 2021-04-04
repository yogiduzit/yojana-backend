package com.yojana.access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

//import com.yojana.helpers.FloatHelper;
import com.yojana.model.project.Project;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;

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
//	@Transactional
//	public void persist(WorkPackage workPackage) {
//		String id;
//		if (workPackage.getParentWPId() == null || workPackage.getParentWPId().isBlank()) {
//        	List<WorkPackage> topLevelWps = getAllWithHierarchyLevel(workPackage.getWorkPackagePk().getProjectID(),
//        			0);
//        	id = PREFIX + Integer.toString(topLevelWps.size() + 1);
//        } else {
//        	List<WorkPackage> children = getChildWPs(workPackage.getWorkPackagePk().getProjectID(),
//        			workPackage.getParentWPId());
//        	id = workPackage.getParentWPId() + SEPERATOR + (children.size() + 1);
//        }
//		
//		workPackage.getWorkPackagePk().setId(id);
//		if (!(workPackage.getParentWPId() == null || workPackage.getParentWPId().isBlank())) {
//			WorkPackage parent = find(new WorkPackagePK(workPackage.getParentWPId(), 
//					workPackage.getWorkPackagePk().getProjectID()));
//			parent.setAllocatedBudget(FloatHelper.add(parent.getAllocatedBudget(), workPackage.getBudget()));
//			parent.setAllocatedInitialEstimate(FloatHelper.add(parent.getAllocatedInitialEstimate(),
//					 workPackage.getInitialEstimate()));
//			parent.setLowestLevel(false);
//			workPackage.setParentWP(parent);
//			workPackage.setHierarchyLevel(parent.getHierarchyLevel() + 1);
//			em.merge(parent);
//		} else {
//			Project parent = workPackage.getProject();
//			parent.setAllocatedBudget(FloatHelper.add(parent.getAllocatedBudget(), workPackage.getBudget()));
//			parent.setAllocatedInitialEstimate(FloatHelper.add(parent.getAllocatedInitialEstimate(),
//					 workPackage.getInitialEstimate()));
//			em.merge(parent); 
//		}
//        em.persist(workPackage);
//    }
	
	/** update an employee. */
	@Transactional
	public void merge(WorkPackage workPackage) {
		WorkPackage old = find(workPackage.getWorkPackagePk());
		if (workPackage.getParentWPId() != null) {
			WorkPackage parent = find(new WorkPackagePK(workPackage.getParentWPId(), 
					workPackage.getWorkPackagePk().getProjectID()));
			
			float parentBudgetAllocation = parent.getAllocatedBudget() - old.getBudget() + workPackage.getBudget();
			float parentEstimateAllocation = parent.getAllocatedInitialEstimate() - old.getInitialEstimate() + workPackage.getInitialEstimate();
			
			parent.setAllocatedBudget(parentBudgetAllocation);
			parent.setAllocatedInitialEstimate(parentEstimateAllocation);
			parent.setLowestLevel(false);
			workPackage.setParentWP(parent);
			em.merge(parent);
		} else {
			Project parent = workPackage.getProject();
			
			float parentBudgetAllocation = parent.getAllocatedBudget() - old.getBudget() + workPackage.getBudget();
			float parentEstimateAllocation = parent.getAllocatedInitialEstimate() - old.getInitialEstimate() + workPackage.getInitialEstimate();
			
			parent.setAllocatedBudget(parentBudgetAllocation);
			parent.setAllocatedInitialEstimate(parentEstimateAllocation);
			
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
		} else {
			Project parent = workPackage.getProject();
			parent.setBudget(parent.getBudget() + workPackage.getAllocatedBudget());
			parent.setInitialEstimate(parent.getInitialEstimate() + workPackage.getInitialEstimate());
			em.merge(parent);
		}
        em.remove(workPackage);
    }
	
	public List<WorkPackage> getAll(String projectId) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId",
                WorkPackage.class); 
		query.setParameter("projectId", projectId);
        List<WorkPackage> workPackages = query.getResultList();
        return workPackages;
    }
	
	public List<WorkPackage> getAllWithHierarchyLevel(String projectId, int hierarchyLevel) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId"
				+ " and w.hierarchyLevel = :hierarchyLevel", WorkPackage.class); 
		query.setParameter("projectId", projectId);
		query.setParameter("hierarchyLevel", hierarchyLevel);
        List<WorkPackage> workPackages = query.getResultList();
        return workPackages;
    }
	
	public List<WorkPackage> getChildWPs(String projectId, String parentId) {       
		TypedQuery<WorkPackage> query = em.createQuery("select w from WorkPackage w where w.workPackagePk.projectID = :projectId"
				+ " and w.parentWPId = :parentId", WorkPackage.class); 
		query.setParameter("projectId", projectId);
		query.setParameter("parentId", parentId);
        List<WorkPackage> workPackages = query.getResultList();
        return workPackages;
    }

}
