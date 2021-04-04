/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.ejb;

import com.farahgee.paypal.entity.SystemUser;
import com.farahgee.paypal.entity.SystemUserGroup;
import com.farahgee.paypal.entity.UserPaypal;
import com.farahgee.paypal.entity.UserTransactionsEn;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


/**
 *
 * @author farahgee
 */
@Stateless
@DeclareRoles({"user","admin"})
public class adminStorageBean implements adminStorage {

    @PersistenceContext
    EntityManager em;
    
    @RolesAllowed("admin")
    
    
    @Override
    public void addAdmin(String username, String adminPassword) {
     
        try {
            //set admin username 
            SystemUser admin = new SystemUser();
            admin.setUsername(username);
            
            //MessageDigest to calculate cryptographic hashing value 
                //SHA-1, SHA-256 --> To find Hash value of text. 
                
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String admin_pw = adminPassword;
            
            //plain text pw converted to hashed pw and stored into DB
            byte[] hash = digest.digest(admin_pw.getBytes(StandardCharsets.UTF_8));
            BigInteger big_int = new BigInteger(1, hash);
            String pw_to_db = big_int.toString(16);
            
            admin.setPassword(pw_to_db);
            //admins pw is set to the hashed version of the pw
            
            SystemUserGroup sys_user_group;
            sys_user_group = new SystemUserGroup(username, "admin");
            
            em.persist(admin);
            em.persist(sys_user_group);
 
        }
         catch (NoSuchAlgorithmException e) {
          System.err.println("Error: compilation Error");
        } 
    }

    
    
    @RolesAllowed("admin")
    @Override
    public boolean adminExists(String username) {
        Query q = em.createNamedQuery("getAdmin"); //run query to get the admin by his/her username
        
        q.setParameter("username", username); //set admins username
        
        
        
         if (!q.getResultList().isEmpty()){
             //return true: if admin with that username exists 
             return true;
         }
         else{
             //return: false if not in DB
             return false;
         }

                
    }
    @RolesAllowed("admin")
    @Override
    public List<UserPaypal> getUserDetails() {
         //retrieve all users from DB
         Query getUserDetails = em.createNamedQuery("getAllUserDetails"); 
         
         //retreive query results, then return a list of all users
         List all_users = getUserDetails.getResultList(); 
         return all_users;
    }
    
    @RolesAllowed("admin")
    @Override
    public List<UserTransactionsEn> getUserTransactions() {
        Query q_transactions = em.createNamedQuery("getUserTransactions");
        
        List UserTransactions = q_transactions.getResultList(); 
        return UserTransactions;
    }  

 
 }
