package com.seoulapp.findhiddenseoul.Model;

/**
 * Created by HOME on 2017-10-18.
 */
public class Token {
    String token;
    String nick_name;
    int level;
    int rank;
    String charURL;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getNick_name() {
        return nick_name;
    }

    public int getLevel() {
        return level;
    }

    public int getRank() {
        return rank;
    }

    public String getCharURL() {
        return charURL;
    }
}
