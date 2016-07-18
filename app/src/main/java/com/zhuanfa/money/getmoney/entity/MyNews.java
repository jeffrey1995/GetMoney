package com.zhuanfa.money.getmoney.entity;

import android.content.Context;

/**
 * Created by tianxiying on 16/7/8.
 */
public class MyNews {
    private String id;
    private String title;
    private int type;
    private double forward_money;
    private String content;
    private String date;
    private String uri_img;
    private String link;

    public MyNews(String id, String title, double forward_money, String content, String date, String uri_img, String link) {
        this.id = id;
        this.title = title;
        this.forward_money = forward_money;
        this.content = content;
        this.date = date;
        this.uri_img = uri_img;
        this.link = link;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getForward_money() {
        return forward_money;
    }

    public void setForward_money(int forward_money) {
        this.forward_money = forward_money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
