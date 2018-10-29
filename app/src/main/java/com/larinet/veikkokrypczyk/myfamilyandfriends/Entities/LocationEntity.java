package com.larinet.veikkokrypczyk.myfamilyandfriends.Entities;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class LocationEntity {
    private String UserName;
    private Date date;
    private GeoPoint geoPoint;
    private String userId;

    public  LocationEntity(){}

    public LocationEntity(String userName, Date date, GeoPoint geoPoint, String userId) {
        this.UserName = userName;
        this.date = date;
        this.geoPoint = geoPoint;
        this.userId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}