package com.farahgee.paypal.ejb;

import com.farahgee.paypal.entity.UserTransactionsEn;
import com.farahgee.paypal.entity.UserPaypal;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * @author farahgee
 */

@Local
public interface userStorage {
    
    public void register(String first_name, String last_name, Date dob, String email, String username, String password, String currency);
         
    public boolean userExists(String username);
    
    public boolean validEmail(String email);
    
    public int sufficientBalance(String username, BigDecimal amount);
   
    public UserPaypal getUsername(String username);
    
    public List<UserTransactionsEn> getUserTransctions(String first_name);
    //1) Users should be able to view all their transactions 
    
    public String makePayment(String username, String recipient, BigDecimal amount);
    //2) make direct payment to other registered users
    
    public void makeRequestedPayment(String first_name, String recipient, BigDecimal amount);
    //3a) request payments from registered users 
    
    public String acceptRequestedPayment(Long paymentRequestID, String sender, String recipient, BigDecimal amount);
    //3b)  accept payments from registered users 
  
    public void rejectRequestedPayment(Long paymentRequestID);
    //3C) reject requested payment from registered user
    public UserPaypal getUserByEmail(String email);

}



    
  
    

