/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.jsf;

import com.farahgee.paypal.ejb.userStorage;
import com.farahgee.paypal.entity.UserPaypal;
import java.math.BigDecimal;
import java.util.Objects;
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
public class paymentBean {
    String recipient;
    BigDecimal amount;
    
    @EJB
    userStorage user_storage;
    public paymentBean(){
        
    }

    public paymentBean(String recipient, BigDecimal amount, userStorage user_storage) {
        this.recipient = recipient;
        this.amount = amount;
        this.user_storage = user_storage;
    }

    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public userStorage getUser_storage() {
        return user_storage;
    }
    public void setUser_storage(userStorage user_storage) {
        this.user_storage = user_storage;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.recipient);
        hash = 71 * hash + Objects.hashCode(this.amount);
        hash = 71 * hash + Objects.hashCode(this.user_storage);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final paymentBean other = (paymentBean) obj;
        if (!Objects.equals(this.recipient, other.recipient)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.user_storage, other.user_storage)) {
            return false;
        }
        return true;
    }
    
   public String makePayment(){
       String sender = "";
       FacesContext context = FacesContext.getCurrentInstance();
       
   //if the user has entered a valid/existing email address   
    if(user_storage.validEmail(recipient)){
        
        sender = context.getExternalContext().getUserPrincipal().getName(); //username of current logged in user
        
            //check to see if the senders balance is suffient>= than the amount that will be sent
            if(user_storage.sufficientBalance(sender, amount) == 1 | user_storage.sufficientBalance(sender, amount)== 0){
                //sender obtained as object 
                UserPaypal user_paypal = user_storage.getUsername(sender);
                    

                        //make sure that the sender is not choosing his/her own email address 
                        if(user_storage.getUserByEmail(sender).equals(recipient)){
                           context.addMessage(null, new FacesMessage("Error Occurred: please do not use your own email address"));
                           return null;
                        }
                        else
                        {
                          String msg = user_storage.makePayment(sender,recipient,amount);
                          
                          if(msg.equals("payment transfer successfully made")){
                             return "transfer successful";
                        }
                        else{
                            context.addMessage(null, new FacesMessage("An unknown error has occured, please try again later"));
                        }
                    }
            }            
            else 
            {
                context.addMessage(null, new FacesMessage("Payment Transfer denied: insufficient funds"));
                return null;
            }
        }        
        else {
            context.addMessage(null, new FacesMessage("This email does not belong to a recipient, please a different email"));
            return null;
        }
        return null;
    }
}

   

