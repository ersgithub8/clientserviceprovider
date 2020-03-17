package com.example.bus_reservation.Model;

public class Dasboard_provider_model {
    public Dasboard_provider_model() {
    }

    public Dasboard_provider_model(String providerid, String name, String image) {
        this.providerid = providerid;
        this.name = name;
        this.image = image;
    }

    public String getProviderid() {
        return providerid;
    }

    public void setProviderid(String providerid) {
        this.providerid = providerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String providerid;
    String name;
    String image;

}
