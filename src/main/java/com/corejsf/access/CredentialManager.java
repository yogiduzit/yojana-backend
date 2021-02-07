/**
 *
 */
package com.corejsf.access;

import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

//    /**
//     * Method to get the Credential by employee number
//     *
//     * @param empNumber, number of the employee whose Credential need to be found
//     * @return Credential, username and password of the employee
//     * @return null, if the emp does not exist
//     * @throws SQLException
//     * @throws DecoderException
//     */
//    public Credential findByToken(String token) throws SQLException, DecoderException {
//        Connection connection = null;
//        PreparedStatement stmt = null;
//        try {
//            try {
//                connection = dataSource.getConnection();
//                try {
//                    stmt = connection.prepareStatement("SELECT * FROM Credential WHERE EmpToken = ?");
//                    stmt.setBytes(1, Hex.decodeHex(token));
//                    final ResultSet result = stmt.executeQuery();
//                    if (result.next()) {
//                        final String password = Hex.encodeHexString(result.getBytes("EmpToken"));
//                        final Credential Credential = new Credential(result.getString("EmpUserName"), password);
//                        Credential.setEmpNumber(result.getInt("EmpNo"));
//                        return Credential;
//                    }
//                } finally {
//                    if (stmt != null) {
//                        stmt.close();
//                    }
//                }
//            } finally {
//                if (connection != null) {
//                    connection.close();
//                }
//            }
//        } catch (final SQLException ex) {
//            ex.printStackTrace();
//            throw ex;
//        }
//        return null;
//    }

    public Credential find(int credId) throws SQLException {
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
    public void insert(Credential Credential) throws SQLException {
    }

    /**
     * Updates an existing Credential record in the datasource
     *
     * @param Credential, Credential object containing username and password
     * @throws SQLException
     */
    public void merge(Credential Credential, int id) throws SQLException {
        
    }

}