/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farahgee.paypal.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import javax.ejb.Stateless;

/**
 *
 * @author farahgee
 */
@Stateless
public class CurrencyConverterBean {
    
    final double GBPtoUSD = 1.22;
    final double USDtoGBP= 0.82;
    final double GBPtoEURO = 1.12;
    final double EUROtoGBP = 0.90; 
    final double USDtoEURO = 0.91;
    final double EUROtoUSD = 1.10;
    
    
    public class currencyConverterBean{
        
    }  
    
    public BigDecimal convert(String sender_currency, String recipient_currency,BigDecimal amount ){
     /*
      the following scenarios:
        - senders currency=GBP && receivers_currency is USD
        -Senders currency=GBP && receivers_currency is euros 
        
        -senders currency=USD && receivers_currency is GBP
        -senders_currency=USD && receivers_currency is Euro
        
        -senders currency=EURO &&receivers_currency is GBP
        -senders_currency= EURO && receivers_currency is USD 
        */
        
     if(sender_currency.equals("GBP") && recipient_currency.equals("USD")){
        
         BigDecimal exchange_rate = new BigDecimal(GBPtoUSD);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount;  
     }
     if(sender_currency.equals("GBP") && recipient_currency.equals("EURO")){
         BigDecimal exchange_rate = new BigDecimal(GBPtoEURO);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount; 
     }
     
     
     if(sender_currency.equals("USD") && recipient_currency.equals("GBP")){
         BigDecimal exchange_rate = new BigDecimal(USDtoGBP);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount; 
     }
     if(sender_currency.equals("USD") && recipient_currency.equals("EURO")){
         BigDecimal exchange_rate = new BigDecimal(USDtoEURO);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount; 
     }
     
     
     if(sender_currency.equals("EURO") && recipient_currency.equals("GBP")){
         BigDecimal exchange_rate = new BigDecimal(EUROtoGBP);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount; 
     }
     if(sender_currency.equals("EURO") && recipient_currency.equals("USD")){
         BigDecimal exchange_rate = new BigDecimal(EUROtoUSD);
         BigDecimal convertedAmount;
         convertedAmount = amount.multiply(exchange_rate).setScale(2, RoundingMode.DOWN);
         return convertedAmount; 
     }
     return null;
  }   
}
