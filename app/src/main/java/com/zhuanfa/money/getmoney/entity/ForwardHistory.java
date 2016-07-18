package com.zhuanfa.money.getmoney.entity;

/**
 * Created by tianxiying on 16/7/14.
 */
public class ForwardHistory {
    private String id;
    private String news_id;
    private String title;
    private String forward_money;
    private int type;
    private String date;
    public final static int TYPE_NEWS_TASK = 0;
    public final static int TYPE_NEW_PEOPLE_TASK = 1;

    public ForwardHistory() {
    }

    public ForwardHistory(String id, String news_id, String title, String forward_money, int type, String date) {
        this.id = id;
        this.news_id = news_id;
        this.title = title;
        this.forward_money = forward_money;
        this.type = type;
        this.date = date;
    }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getForward_money() {
        return forward_money;
    }

    public void setForward_money(String forward_money) {
        this.forward_money = forward_money;
    }
}
