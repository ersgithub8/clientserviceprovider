package com.example.bus_reservation.Model;

public class Wishlist_model {
    public Wishlist_model(String imagelocation, String provider_id, String provider_name, String date_added) {
        this.imagelocation = imagelocation;
        this.provider_id = provider_id;
        this.provider_name = provider_name;
        this.date_added = date_added;
    }

    public Wishlist_model() {
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

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    String   imagelocation;
    String provider_id;
    String   provider_name;
    String   date_added;
}
