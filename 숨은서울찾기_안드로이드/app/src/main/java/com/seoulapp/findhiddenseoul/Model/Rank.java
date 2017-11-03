package com.seoulapp.findhiddenseoul.Model;

/**
 * Created by HOME on 2017-10-24.
 */
public class Rank {

    private String token;

    private int RANK;
    private String level_imgUrl;
    private String nick_name;
    private int level;
    private int counts;

    public void setToken(String token) {
        this.token = token;
    }

    public int getRANK() {
        return RANK;
    }

    public String getLevel_imgUrl() {
        return level_imgUrl;
    }

    public String getNick_name() {
        return nick_name;
    }

    public int getLevel() {
        return level;
    }

    public int getCounts() {
        return counts;
    }

}
