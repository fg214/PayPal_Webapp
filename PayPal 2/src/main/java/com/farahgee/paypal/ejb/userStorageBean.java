/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.ejb;

import com.farahgee.paypal.entity.SystemUser;
import com.farahgee.paypal.entity.SystemUserGroup;
import com.farahgee.paypal.entity.UserPaypal;
import com.farahgee.paypal.entity.UserTransactionsEn;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author farahgee
 */

@DeclareRoles({"user", "admin"})
@Stateless
public class userStorageBean implements userStorage {

    @PersistenceContext EntityManager em;
    
    @EJB
    CurrencyConverterBean currencyConverter;
    
    public userStorageBean(){
        //leave empty
    }
    
    
   @Override
    public void register(String first_name, String last_name, Date dob, String email, String username, String password, String currency) {
        
        try {
            SystemUser system_user;
            SystemUserGroup sys_user_group;
            UserPaypal user_paypal;
            
            //https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
            String passwd = null;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(passwd.getBytes(StandardCharsets.UTF_8));
            BigInteger bigInt = new BigInteger(1, hash);
            String pw_to_db = bigInt.toString(16);
            
            //initialize user_balance to 1000
            BigDecimal user_balance = new BigDecimal(1000.00); 
            
            //create a user on the system with the username and hashed pw
            system_user = new SystemUser(username,pw_to_db);
            //make sure that the new username is in the group "users"
            sys_user_group = new SystemUserGroup(username, "users");
            
            //set the registration details for the UserPaypal
            user_paypal = new UserPaypal(system_user, currency,user_balance,first_name,last_name,dob,email);
            
            em.persist(system_user);
            em.persist(sys_user_group);
            em.persist(user_paypal);
        }
        
        //https://stackoverflow.com/questions/16133881/messagedigest-nosuchalgorithmexception
        catch (NoSuchAlgorithmException e) {
            System.err.println("Error");
        }
    }

    @Override
    public boolean userExists(String username) {
       Query q = em.createNamedQuery("getUser");
       q.setParameter("username", username);
       
       if (!q.getResultList().isEmpty()){
           return true;
       }
       else{
           return false;
       }
    }

    @Override
    public boolean validEmail(String email) {
        Query q = em.createNamedQuery("getEmail");
        q.setParameter("email", email);
        
       if (!q.getResultList().isEmpty()){
           return true;
       }
       else{
          return false; 
       }
        
    }

    @Override
    public int sufficientBalance(String username, BigDecimal amount) {
        Query q = em.createNamedQuery("getuser_balance");//get the User_Paypal balance using username
        q.setParameter("username", username);//username as parameter 
        
        //compare balance to the amount
        BigDecimal user_balance = (BigDecimal) q.getSingleResult();
        
        return user_balance.compareTo(amount);
    }

    @Override
    public UserPaypal getUsername(String username) {
        Query q = em.createNamedQuery("getUser");//run query to get the UserPaypal by their username
        
        q.setParameter("username",username); //parameter is set to users' username 
        
        UserPaypal u = (UserPaypal) q.getSingleResult(); //UserPaypal object
               
        return u;  
    }
    
       
    @Override
    public List<UserTransactionsEn> getUserTransctions(String first_name) {
        Query transactionQuery = em.createNamedQuery("getUserTransaction");
        transactionQuery.setParameter("username", first_name);
        List UserTransactionsEn = transactionQuery.getResultList();
        return UserTransactionsEn;
    }

    @Override
    public String makePayment(String username, String recipient, BigDecimal amount) {
        
        //object: of the sender
        Query senderQuery = em.createNamedQuery("getUser");
        senderQuery.setParameter("username", username);
        UserPaypal sender = (UserPaypal) senderQuery.getSingleResult();
        
        //object of the recipient
        Query recipientQuery = em.createNamedQuery("getUserEmail");
        recipientQuery.setParameter("email", recipient);
        UserPaypal this_recipient = (UserPaypal) recipientQuery.getSingleResult();
        
        //type of transaction: payment
        UserTransactionsEn payment = new UserTransactionsEn("Payment", sender, this_recipient, amount, "Complete");
        
        //if the sender and recipient do not have the same currency
        if (!sender.getCurrency().equals(this_recipient.getCurrency())){
           //use currencyConverter class to convert the sender's currency to match the recipients' currency 
            BigDecimal converted_amount = currencyConverter.convert(sender.getCurrency(), this_recipient.getCurrency(), amount);
            payment.setConvertedAmount(converted_amount);//set converted_amount field, in object:payment
            Query updateRecipientBalance= em.createNamedQuery("updateBalance");
            
            ////UPDATE RECIPIENT BALANCE
            //GET balance of the recipient
           BigDecimal balance = this_recipient.getuser_balance();
           //add amount to their current balance
           BigDecimal newBalance = balance.add(converted_amount);
           //update the balance to the new balance
           updateRecipientBalance.setParameter("newBalance", newBalance);
           updateRecipientBalance.setParameter("email", this_recipient.getEmail());
           updateRecipientBalance.executeUpdate();

           Query updateSenderBalance = em.createNamedQuery("updateBalance");
           
           //UPDATE SENDERS BALANCE
           //get balance of the sender 
           BigDecimal senderBalance = sender.getuser_balance();
           //subtract the amount they transferred/paid out of their current balance
           BigDecimal newSenderBalance = senderBalance.subtract(amount);
           updateSenderBalance.setParameter("newBalance", newSenderBalance);
           updateSenderBalance.setParameter("email", sender.getEmail());
           updateSenderBalance.executeUpdate();
        }
        
        else{
            //otherwise, currencies match, and conversion not needed
            payment.setConvertedAmount(amount);//converted amount ==amount to send (no conversion)
            Query updateRecipientBalance = em.createNamedQuery("updateBalance");
            
            //UPDATE RECIPIENT BALANCE
            BigDecimal balance= this_recipient.getuser_balance();
            BigDecimal newBalance = balance.add(amount);
            updateRecipientBalance.setParameter("newBalance", newBalance);
            updateRecipientBalance.setParameter("email", this_recipient.getEmail());
            updateRecipientBalance.executeUpdate();
            
            
            //UPDATE SENDERS BALANCE
            Query updateSenderBalance = em.createNamedQuery("updateBalance");
            //get sender balance
            BigDecimal senderBalance = sender.getuser_balance();
            BigDecimal newSenderBalance = senderBalance.subtract(amount); //subtract the amount they transferred from their balance
            updateSenderBalance.setParameter("newBalance", newSenderBalance);
            updateSenderBalance.setParameter("email", sender.getEmail());
            
            
        }
        em.persist(payment);
        em.flush();
            
      return "paymentSent"; 
        }

  
    @Override
    public void makeRequestedPayment(String first_name, String recipient, BigDecimal amount) {
         
//get senders UserPaypal object
        Query sender_query = em.createNamedQuery("getUser");
        sender_query.setParameter("username", first_name); //get username by name
        UserPaypal sender =(UserPaypal) sender_query.getSingleResult();//UserPaypal object 
        Query recipientQuery = em.createNamedQuery("getUserEmail");
        recipientQuery.setParameter("email", recipient);//parameter is set to email address of the receiver 
        UserPaypal this_recipient = (UserPaypal) recipientQuery.getSingleResult();// the userpaypal object
        
        UserTransactionsEn paymentRequest= new UserTransactionsEn("Payment Request", sender, this_recipient, amount,"Requested");
        
        em.persist(paymentRequest);   
    }
    
    @Override
    public String acceptRequestedPayment(Long paymentRequestID, String sender, String recipient, BigDecimal amount) {
        try {
            
            Query acceptPaymentRequest = em.createNamedQuery("setPaymentRequestStatus");// run query to change the status of the paymentrequest
            //set new status parameter as accepted
            acceptPaymentRequest.setParameter("status","Accepted");     
            acceptPaymentRequest.setParameter("id",paymentRequestID);
            //payment request status set to accepted
            acceptPaymentRequest.executeUpdate();
            
            // userPaypal object of sender
            Query senderQuery = em.createNamedQuery("getUser");     
            senderQuery.setParameter("username",sender);
            UserPaypal paymentSender = (UserPaypal) senderQuery.getSingleResult();

            // userPaypal object of recipient
            Query recipientQuery = em.createNamedQuery("getUserEmail");   
            recipientQuery.setParameter("email",recipient);
            UserPaypal this_recipient = (UserPaypal) recipientQuery.getSingleResult();
            
            UserTransactionsEn payment;
            //if current user currency is not equal to currency of user that send payment 
            if (!paymentSender.getCurrency().equals(this_recipient.getCurrency()))
            {
                   
            //connver currency from requesters' to current/logged in users currency
            BigDecimal converted_amount = currencyConverter.convert(this_recipient.getCurrency(),paymentSender.getCurrency(), amount);
            
           
            //new user transaction, type:payment, sender:recipient, 
            payment = new UserTransactionsEn("Payment",paymentSender,this_recipient,converted_amount,"Complete");
        
            // convert converted amount back into the payment requester's currency
            BigDecimal convertedRequestedAmount = currencyConverter.convert(paymentSender.getCurrency(), this_recipient.getCurrency(), converted_amount);
            
            payment.setConvertedAmount(convertedRequestedAmount);
            
            
            //UPDATE RECIPIENT BALANCE
            Query updateRecipientBalance = em.createNamedQuery("updateBalance");

            BigDecimal balance = this_recipient.getuser_balance();
            BigDecimal newBalance = balance.add(convertedRequestedAmount);//Add converted amount to balace
            updateRecipientBalance.setParameter("newBalance",newBalance); //update new balance to include amount
            updateRecipientBalance.setParameter("email", this_recipient.getEmail());
            updateRecipientBalance.executeUpdate();
            
            //UPDATE SENDER BALANCE
            Query updateSenderBalance = em.createNamedQuery("updateBalance");

            BigDecimal senderBalance = paymentSender.getuser_balance();//get current balance
            BigDecimal newSenderBalance = senderBalance.subtract(converted_amount);//subtract potential payment from balance
            updateSenderBalance.setParameter("newBalance",newSenderBalance);
            updateSenderBalance.setParameter("email",paymentSender.getEmail());
            updateSenderBalance.executeUpdate();
            
            }
            else
            {
                //otherwise, the payment request being accepted by the user, matches their currency 
                payment = new UserTransactionsEn("Payment",paymentSender,this_recipient,amount,"Complete");//new transaction 
                
                //set amounts to be equal because sender and receiver are transferring in the same currency 
                payment.setConvertedAmount(amount);
                
                //UPDATE RECIPIENTS BALANCE
                Query updateRecipientBalance = em.createNamedQuery("updateBalance");
                
           
                BigDecimal balance = this_recipient.getuser_balance(); //get current balance
                BigDecimal newBalance = balance.add(amount);//add converted amount to their current balance
                updateRecipientBalance.setParameter("newBalance",newBalance);  //update balance using update query 
                updateRecipientBalance.setParameter("email", this_recipient.getEmail());
                updateRecipientBalance.executeUpdate();
            
                //UPDATE SENDERS BALANCE
                Query updateSenderBalance = em.createNamedQuery("updateBalance");
        
                BigDecimal senderBalance = paymentSender.getuser_balance();//get current balance
                BigDecimal newSenderBalance = senderBalance.subtract(amount);//subtract amount to be transferred from balance
                updateSenderBalance.setParameter("newBalance",newSenderBalance);//update balance using update query 
                updateSenderBalance.setParameter("email",paymentSender.getEmail());
                updateSenderBalance.executeUpdate(); 
            }
            
            em.persist(payment);
            em.flush();
            
            return "paymentSent";  

        } 
        catch (EJBTransactionRolledbackException e){
            return "conflict";
        }  
    }



    @Override
    public void rejectRequestedPayment(Long paymentRequestID) {
      
        Query rejectPaymentRequest = em.createNamedQuery("setPaymentRequestStatus");//change status 
        rejectPaymentRequest.setParameter("status","Rejected");//new parameter is set to rejected 
        rejectPaymentRequest.setParameter("id",paymentRequestID);
        rejectPaymentRequest.executeUpdate();//payment request status: rejected 
    }
        

    @Override
    public UserPaypal getUserByEmail(String email) {
        Query recipientQuery = em.createNamedQuery("getEmail");//get UserPaypal by their email
              
        recipientQuery.setParameter("email",email);
        UserPaypal this_recipient = (UserPaypal) recipientQuery.getSingleResult();//obtain UserPaypal object
        
        return this_recipient;
    }

} 
