/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.ejb;

import com.farahgee.paypal.entity.UserPaypal;
import com.farahgee.paypal.entity.UserTransactionsEn;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author farahgee
 */
@Local
public interface adminStorage {
    
    public void addAdmin(String username, String adminPassword);
    
    public boolean adminExists(String username);
    
    public List<UserPaypal> getUserDetails();
    
    public List<UserTransactionsEn> getUserTransactions();
    
        
     
}
