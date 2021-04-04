package com.farahgee.paypal.jsf;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import com.farahgee.paypal.entity.UserPaypal;
import com.farahgee.paypal.entity.UserTransactionsEn;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import com.farahgee.paypal.ejb.userStorage;



/**
 *
 * @author farahgee
 */
//display welcome message using first + Last name of user 


//get account name --> get first + lastname
//get account username --> get username
//display balance


@Named
@RequestScoped
public class welcomepageBean {
    String first_name;
    String last_name;
    String username;
    String currency;
    
    @EJB
    userStorage userStorage;

    public welcomepageBean(String first_name, String last_name, String username, String currency, userStorage userStorage) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.currency = currency;
        this.userStorage = userStorage;
    }

   

    public String get_user_name(){
        String name="";
        FacesContext faces_context = FacesContext.getCurrentInstance();
        ExternalContext external_context = faces_context.getExternalContext();
        
        UserPaypal this_user = userStorage.getUsername(name);
        
        if(external_context.getUserPrincipal() !=null){
            name = external_context.getUserPrincipal().getName();
        }
        return "Welcome to E-Payments " + name;
               
    }
    
    public String get_User_username() {
        
        String username = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("username");
        return username;
    }


   public String displayBalance(){
       String currency_symbol = "";
       
       //get username of current user
       String name = "";
       FacesContext faces_context = FacesContext.getCurrentInstance();
       name = faces_context.getExternalContext().getUserPrincipal().getName();
       
       //get the user based on the username
       UserPaypal this_user = userStorage.getUsername(name);
       
       //determine currency symbol
       if (this_user.getCurrency().equals("GBP")){  
           currency_symbol = "£";
       }
       if (this_user.getCurrency().equals("USD")){
           currency_symbol ="$";
       }
       if (this_user.getCurrency().equalsIgnoreCase("euros")){
           currency_symbol ="€";
       }
       
       return "Hello: "+ name + "Your Account Balance is: " + currency_symbol +  this_user.getuser_balance();
   }

   public List<UserTransactionsEn> getUserTransactions(){
       //username of current user
       String name = "";
       FacesContext faces_context = FacesContext.getCurrentInstance();
       name = faces_context.getExternalContext().getUserPrincipal().getName();
       
       return userStorage.getUserTransctions(name);
   }
   
}

