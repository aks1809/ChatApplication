package com.hidden.calculator;

public class UserList {
    private String user;
    private String key;

    public UserList(){}

    public UserList(String user, String key) {
        this.user = user;
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }
}
