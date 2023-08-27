package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Review implements Serializable {

    public int id;
    public int storeId;
    public int userId;
    public String content;

    public String personUrl;

    public String person;
    public String time;

    public int isLike;
    public String town;
    public String distance;

    public String rating;
    public String name;


    public ArrayList<String> tags;
//    public String tag1;
//    public String tag2;
//    public String tag3;



    public int views;
    public int comments;
    public int likes;


}
