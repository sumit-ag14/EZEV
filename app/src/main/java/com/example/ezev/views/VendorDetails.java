package com.example.ezev.views;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class VendorDetails {

    String vendorId;
    boolean avaiability;
    boolean within_time;
    String full_name,charger_type,email,phone_number;
    Integer price; Double distance;
    GeoPoint loc;
    Timestamp start_time, end_time;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getFull_name() {  return full_name;
    }


    public Timestamp getStart_time() {
        return start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
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
        //avaiability=true;
    }

    public boolean isAvaiability() {
        return avaiability;
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

    public boolean isWithin_time() {
        return within_time;
    }

    public void setWithin_time(boolean within_time) {
        this.within_time = within_time;
    }

    //    public void setAvailability(Boolean availability) {
//        this.availability = availability;
//    }



//    public void setName(String name) {
//        this.full_name = name;
//    }
}
