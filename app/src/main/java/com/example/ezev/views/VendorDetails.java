package com.example.ezev.views;

import com.google.firebase.firestore.GeoPoint;

public class VendorDetails {

    boolean avaiability;
    String full_name,charger_type,email,phone_number;
    Integer price;
    GeoPoint loc;

    public String getFull_name() {
        return full_name;
    }

    public String getCharger_type() {
        return charger_type;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public Integer getPrice() {
        return price;
    }

    public GeoPoint getLoc() {
        return loc;
    }

    //Integer distance;
    public  VendorDetails(){
        avaiability=true;
    }

//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }

    public boolean getAvailability() {
        return avaiability;
    }

//    public void setAvailability(Boolean availability) {
//        this.availability = availability;
//    }



//    public void setName(String name) {
//        this.full_name = name;
//    }
}
