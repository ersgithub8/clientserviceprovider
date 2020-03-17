package com.example.bus_reservation.Model;

public class Categories_model {
    public Categories_model() {
    }

    public Categories_model(String name, String catid, String image) {
        this.name = name;
        this.catid = catid;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String name;
    String catid;
    String image;
}
