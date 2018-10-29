package com.larinet.veikkokrypczyk.myfamilyandfriends.Entities;

public class UserEntity {
    private String UserName;
    private String userId;

    public  UserEntity(){}

    public UserEntity(String userName, String userId) {
        this.UserName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
}
