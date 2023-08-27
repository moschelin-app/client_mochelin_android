package com.musthave0145.mochelins.model;

import java.io.Serializable;

public class PlaceSelect implements Serializable {

    public String name;
    public String vicinity;

    // 변수 이름은 무조건, JSON 키값과 일치해야한다 무조건!!!!!
    // 클래스 만드는게 헷갈리면 밖에 만들자 제발!!!!!!!!!!
    public Geometry geometry;

    public class Geometry implements Serializable {

        public Location location;

        public class Location implements Serializable{

            // 무조건 변수 이름은 타이핑 말구 카피해서 들고오쟈!!!
            // 오타 방지!!!!!!!! 무조건!!!!!!!!
            // 직렬화는 무조건 일관성있게 모든 클래스에 해줘야한다!!!!제ㅐ발!!!!!
            public double lat;
            public double lng;
        }
    }

}


