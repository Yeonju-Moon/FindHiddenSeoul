package com.seoulapp.findhiddenseoul.ListViewItem;

/**
 * Created by BANGYURA on 2017-09-22.
 */

public class ListViewItem {
    private String heritage_index;
    private String heritage_name;
    private String category;
    private String gu;
    private String heritage_mainImg;
    private String likes;
    private String nickname;

    private String url;

    private String review_img;
    private String review_content;
    private String review_index;
    private String review_heritage_index;


    //GET
    public String getHeritage_index() {
        return heritage_index;
    }

    public String getHeritage_name() {
        return heritage_name;
    }

    public String getCategory() {
        return category;
    }

    public String getGu() {
        return gu;
    }

    public String getHeritage_mainImg() {
        return heritage_mainImg;
    }

    public String getReview_img() {
        return review_img;
    }

    public String getReview_content() {
        return review_content;
    }

    public String getReview_index() {
        return review_index;
    }

    public String getLikes() {
        return likes;
    }

    public String getNickname() {
        return nickname;
    }

    public String getReview_heritage_index(){
        return review_heritage_index;
    }

    //SET
    public void setHeritage_index(String heritage_index) {
        this.heritage_index = heritage_index;
    }

    public void setHeritage_name(String heritage_name) {
        this.heritage_name = heritage_name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGu(String gu) {
        this.gu = gu;
    }

    public void setHeritage_mainImg(String heritage_mainImg) {
        this.heritage_mainImg = heritage_mainImg;
    }

    public void setReview_img(String review_img) {
        this.review_img = review_img;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public void setReview_index(String review_index) {
        this.review_index = review_index;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setReview_heritage_index(String review_heritage_index){
        this.review_heritage_index = review_heritage_index;
    }
}
