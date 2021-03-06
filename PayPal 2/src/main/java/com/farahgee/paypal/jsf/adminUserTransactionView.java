/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import com.farahgee.paypal.ejb.userStorage;
import com.farahgee.paypal.entity.UserTransactionsEn;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
public class adminUserTransactionView {
    String username; //will be searched for 
    List<UserTransactionsEn> transactions;//the transaction of that user
    
    @EJB
    userStorage user_storage ;

    public adminUserTransactionView() {
    }

    public adminUserTransactionView(String username, List<UserTransactionsEn> transactions, userStorage user_storage) {
        this.username = username;
        this.transactions = transactions;
        this.user_storage = user_storage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserTransactionsEn> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<UserTransactionsEn> transactions) {
        this.transactions = transactions;
    }

    public userStorage getUser_storage() {
        return user_storage;
    }

    public void setUser_storage(userStorage user_storage) {
        this.user_storage = user_storage;
    }
    
    public void transaction_search(){
        //if username is in database
        if(user_storage.userExists(username)){
            List <UserTransactionsEn> userTransactions = user_storage.getUserTransctions(username);
            transactions = userTransactions;
        }
        else{
            //if the username does not exist in the DB
            FacesContext cntx = FacesContext.getCurrentInstance();
            cntx.addMessage(null, new FacesMessage("User not found in DB//does not exist"));
        }
    }   
        
        public String format(BigDecimal amount, String currency) {
                
        String currencySymbol = null;
        
        if (currency.equals("GBP"))
        {
            currencySymbol = "??";
        }
        
        if (currency.equalsIgnoreCase("EUROS"))
        {
            currencySymbol = "???";
        }
        
        if (currency.equals("USD"))
        {
            currencySymbol = "$";
        }
        
        if (amount == null)
        {
            return "";
        }
        else
        {
            return currencySymbol + amount.setScale(2, RoundingMode.DOWN).toString(); 
        }
    }
        
 }

    

