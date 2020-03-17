package com.example.bus_reservation.Model;

public class Membership_model {
    String mambershipid;
    String   name;
    String  price;
    String  see;
    String buy;

    public Membership_model(String mambershipid, String name, String price, String see, String buy) {
        this.mambershipid = mambershipid;
        this.name = name;
        this.price = price;
        this.see = see;
        this.buy = buy;
    }

    public Membership_model() {
    }

    public String getMambershipid() {
        return mambershipid;
    }

    public void setMambershipid(String mambershipid) {
        this.mambershipid = mambershipid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSee() {
        return see;
    }

    public void setSee(String see) {
        this.see = see;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }
}

