/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import com.farahgee.paypal.ejb.adminStorage;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author farahgee
 */
@Named
@RequestScoped
public class addAdminBean {
    @EJB
    adminStorage admin_storage;
    
    String username;
    String adminpassword;

    public addAdminBean() {
    }

    public addAdminBean(adminStorage admin_storage, String username, String adminpassword) {
        this.admin_storage = admin_storage;
        this.username = username;
        this.adminpassword = adminpassword;
    }

    public adminStorage getAdmin_storage() {
        return admin_storage;
    }

    public void setAdmin_storage(adminStorage admin_storage) {
        this.admin_storage = admin_storage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }
    
    public String add_admin(){
        if(admin_storage.adminExists(username)){
            FacesContext ctxt = FacesContext.getCurrentInstance();
            ctxt.addMessage(null, new FacesMessage("It Looks like you're already an admin. Try signing in"));
            return null;
        }
        else{
            //if admin does not already exist, add to DB
            admin_storage.addAdmin(username, adminpassword);
            return "You're now an admin";
         
        }
    }
    
}
