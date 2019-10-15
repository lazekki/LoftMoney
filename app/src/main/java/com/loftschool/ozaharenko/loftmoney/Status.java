package com.loftschool.ozaharenko.loftmoney;

import com.google.gson.annotations.SerializedName;

public class Status {

    private String status;

    private int id;

    @SerializedName("auth_token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
