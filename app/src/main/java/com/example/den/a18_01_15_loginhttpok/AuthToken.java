package com.example.den.a18_01_15_loginhttpok;

/**
 * Created by Den on 1/15/2018.
 */

public class AuthToken {
    static final String TOKEN = "TOKEN";
    static final String TOKEN_STORAGE = "Auth";
    private String token;

    public AuthToken() {
    }

    public AuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
