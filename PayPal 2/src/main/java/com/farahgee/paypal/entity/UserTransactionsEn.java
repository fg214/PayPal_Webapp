/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author farahgee
 */

@NamedQueries ({

    @NamedQuery(name="getTransactions", query="SELECT t FROM UserTransactions t WHERE t.sender.user_id.username = :username OR t.recipient.user_id.username = :username"),
    @NamedQuery(name="getPaymentRequests", query="SELECT t FROM UserTransactions t WHERE t.recipient.user_id.username = :username AND t.transactionStatus = :status"),
    @NamedQuery(name="setPaymentRequestStatus", query="UPDATE UserTransactions t SET t.transactionStatus =:status WHERE t.transactionId = :id"),
    @NamedQuery(name="getAllTransactions", query="SELECT t FROM UserTransactions t")

})
@Entity
public class UserTransactionsEn implements Serializable {
 @Id
 @GeneratedValue (strategy = GenerationType.IDENTITY)
 @Column(name="Transaction_ID")
    
 
 //sender, receiver, amount, transactionid, transactiontype   
    private long transacionId;;
    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private String transaction_type;
    private String transaction_status;
    @OneToOne
    private UserPaypal sender;
    @OneToOne
    private UserPaypal recipient;

    public UserTransactionsEn(long transacionId, BigDecimal amount, BigDecimal convertedAmount, String transaction_type, String transaction_status, UserPaypal sender, UserPaypal recipient) {
        this.transacionId = transacionId;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.transaction_type = transaction_type;
        this.transaction_status = transaction_status;
        this.sender = sender;
        this.recipient = recipient;

    }

    public UserTransactionsEn(String payment, UserPaypal sender, UserPaypal this_recipient, BigDecimal amount, String complete) {
        
    }


    public long getTransacionId() {
        return transacionId;
    }

    public void setTransacionId(long transacionId) {
        this.transacionId = transacionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public UserPaypal getSender() {
        return sender;
    }

    public void setSender(UserPaypal sender) {
        this.sender = sender;
    }

    public UserPaypal getRecipient() {
        return recipient;
    }

    public void setRecipient(UserPaypal recipient) {
        this.recipient = recipient;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.transacionId ^ (this.transacionId >>> 32));
        hash = 43 * hash + Objects.hashCode(this.amount);
        hash = 43 * hash + Objects.hashCode(this.convertedAmount);
        hash = 43 * hash + Objects.hashCode(this.transaction_type);
        hash = 43 * hash + Objects.hashCode(this.transaction_status);
        hash = 43 * hash + Objects.hashCode(this.sender);
        hash = 43 * hash + Objects.hashCode(this.recipient);
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
        final UserTransactionsEn other = (UserTransactionsEn) obj;
        if (this.transacionId != other.transacionId) {
            return false;
        }
        if (!Objects.equals(this.transaction_type, other.transaction_type)) {
            return false;
        }
        if (!Objects.equals(this.transaction_status, other.transaction_status)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.convertedAmount, other.convertedAmount)) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.recipient, other.recipient)) {
            return false;
        }
        return true;
    }

    
 
}
