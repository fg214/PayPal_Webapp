/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import com.farahgee.paypal.ejb.adminStorage;
import com.farahgee.paypal.ejb.adminStorageBean;
import com.farahgee.paypal.entity.SystemUser;
import com.farahgee.paypal.entity.UserPaypal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author farahgee
 */
//get userdetails, and currency_format

@Named 
@RequestScoped
public class adminUserViewBean {
    
    @EJB
    adminStorage admin_storage;

     
    public adminUserViewBean() {
        }
    
    
    public List<UserPaypal> getUserDetails(){

      return admin_storage.getUserDetails();
   }
    
    public String currency_format(BigDecimal amount, String currency){
        String currency_symbol = "";
        if (currency.equals("GBP")){
            currency_symbol = "£";
        }
        if (currency.equals("USD")){
            currency_symbol ="$";
        }
        if (currency.equalsIgnoreCase("EUROS")){
            currency_symbol ="€";
        }
        if (amount == null){
            currency_symbol = "";
        }
        
        
        return currency_symbol + amount;
    }
            
}     

   

