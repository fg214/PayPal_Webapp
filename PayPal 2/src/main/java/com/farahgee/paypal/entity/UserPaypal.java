/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 *
 * @author farahgee
 */

//getuser, getemail, updateBalance, getallusers, getbalance, getbalancebyemail
@NamedQueries({
    @NamedQuery(name="getUser", query="SELECT this_user FROM UserPaypal this_user WHERE this_user.userId = :username "),
    @NamedQuery(name="getEmail", query="SELECT this_user From UserPaypal this_user WHERE this_user.email = :email"),
    @NamedQuery(name="updateBalance", query="UPDATE UserPaypal this_user SET this_user.user_balance =:newBalance WHERE this_user.email = :email"),
    @NamedQuery(name="getAllUsers", query="SELECT this_user FROM UserPaypal this_user"),
    @NamedQuery(name="getuser_balance", query = "SELECT this_user.balance FROM UserPaypal this_user WHERE this_user.userId.username = :username"),
    @NamedQuery(name="getBalanceByEmail", query = "SELECT this_user.balance FROM UserPaypal this_user WHERE this_user.email = :email")
})

@Entity
public class UserPaypal implements Serializable {
   
@Id //id field: marks a field as a primary key field, when a PK field is defined, it is automatically injected into that field by objectDB
@OneToOne
@GeneratedValue (strategy = GenerationType.IDENTITY)
//@JoinColumn(name="USER_ID")

//private Long id;

private String first_name;

private String last_name;

private SystemUser userId;

private SystemUser system_user;

private Date dob;

private String email;

private String currency;

private BigDecimal user_balance;

private UserPaypal user_paypal;

@Version
private Long version;


    public UserPaypal(SystemUser system_user, String currency, BigDecimal user_balance, String first_name, String last_name, Date dob, String email, UserPaypal user_paypal) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.userId = userId;
        this.dob = dob;
        this.email = email;
        this.currency = currency;
        this.user_balance = user_balance;
        this.system_user = system_user;
        this.user_paypal = user_paypal;
    }

    public UserPaypal(SystemUser system_user, String currency, BigDecimal user_balance, String first_name, String last_name, Date dob, String email) {
        
    }
    
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public SystemUser getUserId() {
        return userId;
    }

    public void setUserId(SystemUser userId) {
        this.userId = userId;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getuser_balance() {
        return user_balance;
    }

    public void setUser_balance(BigDecimal user_balance) {
        this.user_balance = user_balance;
    }

   //to avoid entities and their associations-->
   //override their @equals and @hashcode methods

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.first_name);
        hash = 37 * hash + Objects.hashCode(this.last_name);
        hash = 37 * hash + Objects.hashCode(this.userId);
        hash = 37 * hash + Objects.hashCode(this.dob);
        hash = 37 * hash + Objects.hashCode(this.email);
        hash = 37 * hash + Objects.hashCode(this.currency);
        hash = 37 * hash + Objects.hashCode(this.user_balance);
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
        final UserPaypal other = (UserPaypal) obj;
        if (!Objects.equals(this.first_name, other.first_name)) {
            return false;
        }
        if (!Objects.equals(this.last_name, other.last_name)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.dob, other.dob)) {
            return false;
        }
        if (!Objects.equals(this.user_balance, other.user_balance)) {
            return false;
        }
        return true;
    }
  
}
