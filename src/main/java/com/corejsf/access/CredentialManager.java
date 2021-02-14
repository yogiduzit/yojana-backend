/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
@Named("CredentialManager")
@ConversationScoped
public class CredentialManager implements Serializable {
	
//    private PasswordHelper passwordHelper;

    /**
     * Variable for implementing serializable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

    public CredentialManager() {
//        try {
//            passwordHelper = new PasswordHelper();
//        } catch (final NoSuchAlgorithmException e) {
//            passwordHelper = null;
//            e.printStackTrace();
//        }
    }

    public Credential find(UUID credId) throws SQLException {
    	return em.find(Credential.class, credId);
    }
    
    public Credential find(String empUserName) throws SQLException {
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
    public void persist(Credential credential) throws SQLException {
        em.persist(credential);
    }

    /**
     * Updates an existing Credential record in the datasource
     *
     * @param Credential, Credential object containing username and password
     * @throws SQLException
     */
	@Transactional
    public void merge(Credential credential) throws SQLException {
        em.merge(credential);
    }
    
	@Transactional
    public void remove(Credential credential) throws SQLException {
        credential = find(credential.getId());
        em.remove(credential);
    }
    
    public List<Credential> getAll() {
        TypedQuery<Credential> q = em.createQuery("SELECT c FROM Credential c", Credential.class);
        List<Credential> creds = q.getResultList();
        return creds;
    }

}