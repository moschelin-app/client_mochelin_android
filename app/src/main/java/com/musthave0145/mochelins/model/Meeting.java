package com.musthave0145.mochelins.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Meeting implements Serializable {
    public int id;
    public int userId;
    public int storeId;
    public String content;
    public String date;
    public String photo;
    public String profile;
    public String storeName;
    public double storeLat;
    public double storeLng;
    public double distance;
    public int attend;
    public int maximum;
    public String nickname;

    public ArrayList<Profile> profiles;

    public class Profile implements Serializable {
        public String profile;
    }
}
