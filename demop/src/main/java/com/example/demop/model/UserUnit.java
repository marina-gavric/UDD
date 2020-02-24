package com.example.demop.model;

import java.util.ArrayList;

public class UserUnit {
    private String username;
    private ArrayList<Float> locationPoints = new ArrayList<Float>();

    public UserUnit(){}
    public UserUnit(String u, Float longitude, Float latitude){
        locationPoints.add(latitude);
        locationPoints.add(longitude);
        setUsername(u);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Float> getLocationPoints() {
        return locationPoints;
    }

    public void setLocationPoints(ArrayList<Float> locationPoints) {
        this.locationPoints = locationPoints;
    }
}
