package com.example.bus_reservation.Model;

public class Chat_model {
    String id;
    String name;
    String imagelocation;
    String provider_id;

    public Chat_model() {
    }

    public Chat_model(String id, String name, String imagelocation, String provider_id) {
        this.id = id;
        this.name = name;
        this.imagelocation = imagelocation;
        this.provider_id = provider_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagelocation() {
        return imagelocation;
    }

    public void setImagelocation(String imagelocation) {
        this.imagelocation = imagelocation;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }
}
