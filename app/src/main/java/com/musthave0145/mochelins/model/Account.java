package com.musthave0145.mochelins.model;



public class Account {

     public String storeName;
     public String price;
     public String payment;
     public String date;
     public String content;


     public Account(String storeName, String price, String payment, String date, String content) {
          this.storeName = storeName;
          this.price = price;
          this.payment = payment;
          this.date = date;
          this.content = content;
     }

     public Account(String storeName, String price, String payment, String date) {
          this.storeName = storeName;
          this.price = price;
          this.payment = payment;
          this.date = date;
     }
}
