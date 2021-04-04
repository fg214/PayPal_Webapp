package com.farahgee.paypal.jsf;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import com.farahgee.paypal.ejb.userStorage;


/**
 *
 * @author farahgee
 */

//gets: users first and lastname from DB
//displays welcome message with users name

@Named
@RequestScoped
public class loginBean {
    String username;
    String password;
    
    @EJB
    userStorage userStorage;

    public loginBean(String username, String password) {
        this.username = username;
        this.password = password;
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
    
    public String login(){
        
       String navResult = "";
       System.out.println("Entered Username is= " + username + ", password is= " + password);
       if (username.equalsIgnoreCase(navResult) && password.equals(navResult)){
           navResult = "success";
       }
       else{
           navResult= "failure";
       }
       return navResult;
    }    
}
