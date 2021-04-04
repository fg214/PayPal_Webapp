/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import com.farahgee.paypal.ejb.userStorage;

/**
 *
 * @author farahgee
 */


@RequestScoped
public class registrationBean {
    
    String first_name;
    String last_name;
    Date dob;
    String email;
    String username;
    String password;
    String confirm_password;
    String currency;
            
    @EJB
    userStorage userStorage;

    public registrationBean(String first_name, String last_name, Date dob, String email, String username, String password, String confirm_password, String currency, userStorage userStorage) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirm_password = confirm_password;
        this.currency = currency;
        this.userStorage = userStorage;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public userStorage getUserStorage() {
        return userStorage;
    }

    public void setUserStorage(userStorage userStorage) {
        this.userStorage = userStorage;
    }
    
    /**
    public String register_test(){
        
        if (userStorage.userExists(username)){
            //error if user already exists 
            
        }
        
       //otherwise check if password and confirm_password match 
            //inform user if they dont match
            
            //if user and confirmation_password match 
            //check that the email address does not already exist in the database
            
                //notify user if email already exists in database
                
                //otherwise, register the user 
                //return the user to the welcome page 
    }
    **/
    
    public String register(){
        boolean stored =true;
        FacesMessage message = null;
        String outcome = null;
        //check to see if user exists 
        if(stored){
            //if user doesnt exist record, register new user
            message = new FacesMessage("User registered successfully");
            outcome ="success";
            //but first check that the password and confirmation password match
        }
        else{
            message = new FacesMessage("User exists, please go to login page");
            outcome = "login";           
        }
        FacesContext.getCurrentInstance().addMessage(null, message);
        return outcome;
    } 


    private boolean Password_confirmation(String password, String confirm_password) {
        return password.equals(confirm_password);
    }
}
