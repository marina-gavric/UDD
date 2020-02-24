package com.example.demop.model;

import java.util.ArrayList;

public class UserUnit {
    private String username;
    private ArrayList<Float> location = new ArrayList<Float>();

    public UserUnit(){}
    public UserUnit(String u, Float latitude, Float longitude){
        location.add(latitude);
        location.add(longitude);
        setUsername(u);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Float> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Float> location) {
        this.location = location;
    }
}
