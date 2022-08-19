package com.example.xiaomaibu.Bean;

import android.graphics.Bitmap;

public class Goods {
    private String gid;
    private String aid;
    private String name;
    private String img;
    private float price;
    private float cost;
    private int num;
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setPrice(int price) {
        this.price = price;
    }



    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }




    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "gid='" + gid + '\'' +
                ", aid='" + aid + '\'' +
                ", name='" + name + '\'' +
                ", img=" + img +
                ", price=" + price +
                ", cost=" + cost +
                ", num=" + num +
                ", sort='" + sort + '\'' +
                '}';
    }
}
