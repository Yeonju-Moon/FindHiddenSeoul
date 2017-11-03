package com.seoulapp.findhiddenseoul.Model;

/**
 * Created by JIYOUNGKIM on 2017-10-22.
 */

public class Review {

    private String token;
    private String review_img;
    private String review_content;
    private String review_index;
    private String heritage_index;
    private String review_date;
    private String mine;
    private String levelup;
    private String nick_name;
    private String review_heritage_index;

    public String getReview_heritage_index() {
        return review_heritage_index;
    }

    public void setReview_heritage_index(String review_heritage_index) {
        this.review_heritage_index = review_heritage_index;
    }

    public String getHeritage_index() {
        return heritage_index;
    }

    public void setHeritage_index(String heritage_index) {
        this.heritage_index = heritage_index;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getMine() {

        return mine;
    }

    public void setMine(String mine) {

        this.mine = mine;
    }

    public String getReview_date() {

        return review_date;
    }

    public void setReview_date(String review_date) {

        this.review_date = review_date;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public String getReview_img() {

        return review_img;
    }

    public void setReview_img(String review_img) {

        this.review_img = review_img;
    }

    public String getReview_content() {

        return review_content;
    }

    public void setReview_content(String review_content) {

        this.review_content = review_content;
    }

    public String getReview_index() {

        return review_index;
    }

    public void setReview_index(String review_index) {

        this.review_index = review_index;
    }

    public String getLevelup() {
        return levelup;
    }

    public void setLevelup(String levelup) {
        this.levelup = levelup;
    }

}
