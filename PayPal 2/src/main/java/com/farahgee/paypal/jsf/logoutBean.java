/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author farahgee
 */
@Named
@RequestScoped
public class logoutBean {

    public String logout(){
        FacesContext ctxt = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctxt.getExternalContext().getRequest();
    
    try{
        request.logout();
        return "logout";
    }        
    catch(ServletException e){
        ctxt.addMessage(null, new FacesMessage("error logging out"));
        return null;
        }
    }
}
