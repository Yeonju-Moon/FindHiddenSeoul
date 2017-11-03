package com.seoulapp.findhiddenseoul.Model;

/**
 * Created by HOME on 2017-10-31.
 */
public class Find {

    private String phone;
    private String userId;
    private String new_passwd;
    private String find_pw2;

    public String getFind_pw2() {
        return find_pw2;
    }

    public void setNew_passwd(String new_passwd) {
        this.new_passwd = new_passwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserId() {
        return userId;
    }
}