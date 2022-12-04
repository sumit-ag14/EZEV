package com.example.ezev.views;

import com.google.firebase.Timestamp;

public class TransactionDetails {
    String vendor_name;
    long price;
    Timestamp date;
    String visible_day;

    public String getVisible_day() {
        return visible_day;
    }

    public void setVisible_day(String visible_day) {
        this.visible_day = visible_day;
    }

    public  TransactionDetails(){
        //avaiability=true;
    }
    public Timestamp getTimestamp() {
        return date;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
//    public void setTimestamp(Timestamp timestamp) {
//        this.date = timestamp;
//    }

//    public String getDate() {
//        return date_string_vala;
//    }
//
//    public void setDate(String date) {
//        this.date_string_vala = date;
//    }

    public long getAmount() {
        return price;
    }

    public String getStation_name() {
        return vendor_name;
    }
}
