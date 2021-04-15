/**
 * 
 */
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

import com.yojana.model.estimate.Estimate;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.timesheet.TimesheetRow;

/**
 * @author yogeshverma
 *
 */
@Dependent
@Stateless
public class ReportManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7675534862978318981L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	
	public Map<String, Map<String, Object>> generateMonthlyReport(String projectId, LocalDate date) {
		Map<String, Map<String, Object>> report = new HashMap<>();
		TypedQuery<WorkPackage> query = em.createQuery(
				"select DISTINCT w from WorkPackage w"
				+ " LEFT JOIN FETCH w.estimates e"
				+ " LEFT JOIN FETCH w.childWPs c"
				+ " LEFT JOIN FETCH e.rows er"
				+ " LEFT JOIN FETCH w.rows tr"
				+ " LEFT JOIN FETCH tr.timesheet t"
				+ " LEFT JOIN FETCH t.employee e"
				+ " where w.workPackagePk.projectID = :projectId",
                WorkPackage.class); 
		query.setParameter("projectId", projectId);
		List<WorkPackage> wps = query.getResultList();
        Set<WorkPackage> workPackages = new TreeSet<WorkPackage>(wps);
        
        for (WorkPackage wp: workPackages) {
        	String id = wp.getWorkPackagePk().getId();
        	if (report.containsKey(id)) {
        		continue;
        	}
        	
        	if (wp.getIsLowestLevel()) {
        		report.put(id, extractLowestLevelWorkPackageMonthlyData(wp));
        		continue;
        	}
        	
        	Map<String, Object> data = new HashMap<>();
        	report.put(id, data);
        	
        	Double charge = 0.0;
        	Double planned = 0.0;
        	Double costToComplete = 0.0;
        	Double costAtCompletion = 0.0;
        	Double initialEstimate = wp.getInitialEstimate();
        	Double budget = wp.getBudget();
        	
        	for (WorkPackage child: wp.getChildWPs()) {
        		String childId = child.getWorkPackagePk().getId();
        		if (!child.getIsLowestLevel()) {
        			continue;
        		}
        		if (report.containsKey(childId)) {
        			charge += (Double) report.get(childId).get("charge");
        			planned += (Double) report.get(childId).get("planned");
        			costToComplete += (Double) report.get(childId).get("costToComplete");
        			costAtCompletion += (Double) report.get(childId).get("costAtCompletion");
        		} else {
        			Map<String, Object> childData = extractLowestLevelWorkPackageMonthlyData(child);
        			charge += (Double) childData.get("charge");
        			planned += (Double) childData.get("planned");
        			costToComplete += (Double) childData.get("costToComplete");
        			costAtCompletion += (Double) childData.get("costAtCompletion");
        			report.put(childId, childData);
        		}
        	}
        	
        	data.put("id", wp.getWorkPackagePk().getId());
    		data.put("charge", charge);
    		data.put("planned", planned);
    		data.put("budget", budget);
    		data.put("costToComplete", costToComplete);
    		data.put("costAtCompletion", costAtCompletion);
    		data.put("initialEstimate", initialEstimate);
        	
        }
        
        return report;
	}
	
	public Map<String, Map<String, Object>> generateWeeklyReport(String projectId, LocalDate date) {
		Map<String, Map<String, Object>> report = new HashMap<>();
		TypedQuery<WorkPackage> query = em.createQuery(
				"select DISTINCT w from WorkPackage w"
				+ " LEFT JOIN FETCH w.childWPs c"
				+ " LEFT JOIN FETCH w.rows tr"
				+ " LEFT JOIN FETCH tr.timesheet t"
				+ " LEFT JOIN FETCH t.employee e"
				+ " where w.workPackagePk.projectID = :projectId",
                WorkPackage.class); 
		query.setParameter("projectId", projectId);
		List<WorkPackage> wps = query.getResultList();
        Set<WorkPackage> workPackages = new TreeSet<WorkPackage>(wps);
        
        for (WorkPackage wp: workPackages) {
        	String id = wp.getWorkPackagePk().getId();
        	if (report.containsKey(id)) {
        		continue;
        	}
        	
        	if (wp.getIsLowestLevel()) {
        		report.put(id, extractLowestLevelWorkPackageWeeklyData(wp));
        		continue;
        	}
        	
        	Map<String, Object> data = new HashMap<>();
        	report.put(id, data);
        	
        	Double charge = 0.0;
        	Double chargeHours = 0.0;
        	
        	for (WorkPackage child: wp.getChildWPs()) {
        		String childId = child.getWorkPackagePk().getId();
        		if (!child.getIsLowestLevel()) {
        			continue;
        		}
        		if (report.containsKey(childId)) {
        			charge += (Double) report.get(childId).get("charge");
        			chargeHours += (Double) report.get(childId).get("chargeHours");
        		} else {
        			Map<String, Object> childData = extractLowestLevelWorkPackageWeeklyData(child);
        			charge += (Double) childData.get("charge");
        			chargeHours += (Double) childData.get("hours");
        			report.put(childId, childData);
        		}
        	}
        	
        	data.put("id", wp.getWorkPackagePk().getId());
    		data.put("charge", charge);
    		data.put("hours", chargeHours);
        	
        }
        
        return report;
	}
	
	private Map<String, Object> extractLowestLevelWorkPackageMonthlyData(WorkPackage wp) {
		Map<String, Object> data = new HashMap<>();
		
		Double charge = 0.0;
    	Double planned = 0.0;
    	Double costToComplete = 0.0;
    	Double costAtCompletion = 0.0;
    	Double initialEstimate = 0.0;
    	Double budget = 0.0;
    	
    	for(TimesheetRow row: wp.getRows()) {
			charge += (row.getSum() * row.getTimesheet().getEmployee().getLabourGrade().getChargeRate());
		}
		Estimate weekly = null;
		for (Estimate estimate: wp.getEstimates()) {
			switch (estimate.getType()) {
	    	case initial:
	    		initialEstimate = estimate.getEstimateToComplete();
	    		break;
	    	case planned:
	    		planned = estimate.getEstimateToComplete();
	    		break;
	    	case weekly:
	    		if (weekly == null) {
	    			weekly = estimate;
	    		} else { 
	    			if (!(weekly.getAudit().getCreatedAt().compareTo(estimate.getAudit().getCreatedAt()) > 0)) {
	    				weekly = estimate;
	    			}
	    		}
	    	default:
	    		break;
	    	}
		}
		if (weekly != null) {
			costToComplete = weekly.getEstimateToComplete();
		}
		costAtCompletion = costToComplete + charge;
		
		data.put("id", wp.getWorkPackagePk().getId());
		data.put("charge", charge);
		data.put("planned", planned);
		data.put("budget", budget);
		data.put("costToComplete", costToComplete);
		data.put("costAtCompletion", costAtCompletion);
		data.put("initialEstimate", initialEstimate);
		
		return data;
	}
	
	private Map<String, Object> extractLowestLevelWorkPackageWeeklyData(WorkPackage wp) {
		Map<String, Object> data = new HashMap<>();
		
		Double charge = 0.0;
		Double chargeHours = 0.0;
		
		Map<Integer, Map<String, Object>> details = new HashMap<>();
    	
    	for(TimesheetRow row: wp.getRows()) {
			charge += (row.getSum() * row.getTimesheet().getEmployee().getLabourGrade().getChargeRate());
			chargeHours += row.getSum();
			
			int employeeId = row.getTimesheet().getOwnerId();
			if (details.containsKey(employeeId)) {
				Double prev = (Double) details.get(employeeId).get("charge");
				details.get(employeeId).put("charge", prev + row.getSum() * row.getTimesheet().getEmployee().getLabourGrade().getChargeRate());
			} else {
				details.put(employeeId, new HashMap<>());
				details.get(employeeId).put("empId", employeeId);
				details.get(employeeId).put("empName", row.getTimesheet().getEmployee().getFullName());
				details.get(employeeId).put("charge", row.getSum() * row.getTimesheet().getEmployee().getLabourGrade().getChargeRate());
				details.get(employeeId).put("payGrade", row.getTimesheet().getEmployee().getLabourGrade());
			}
		}
		
		data.put("id", wp.getWorkPackagePk().getId());
		data.put("charge", charge);
		data.put("hours", chargeHours);
		data.put("details", details);
		
		return data;
	}

}
