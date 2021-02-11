/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.corejsf.model.employee.Credential;

/**
 * This is the class called CredentialManager
 *
 * @author Yogesh Verma
 * @version 1.0
 *
 */
@Dependent
public class CredentialManager implements Serializable {

    /**
     * Variable for implementing serializable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

    public CredentialManager() {}

    public Credential find(UUID id) {
    	return em.find(Credential.class, id);
    }
    
    public Credential find(String empUserName) {
    	return (Credential) em.createNamedQuery("Credential.findByUsername")
    			.setParameter("username", empUserName)
    			.getSingleResult();
    }

    /**
     * Adds a Credential record to the Credential table in the datasource
     *
     * @param Credential, Credential object containing username and password
     * @throws SQLException
     */
    @Transactional
    public void persist(Credential credential) {
        em.persist(credential);
    }

    /**
     * Updates an existing Credential record in the datasource
     *
     * @param Credential, Credential object containing username and password
     * @throws SQLException
     */
    public void merge(Credential credential) {
        em.merge(credential);
    }
    
    public void remove(Credential credential) {
        credential = find(credential.getId());
        em.remove(credential);
    }
    
    public List<Credential> getAll() {
        TypedQuery<Credential> q = em.createQuery("SELECT c FROM Credential c", Credential.class);
        List<Credential> creds = q.getResultList();
        return creds;
    }

}