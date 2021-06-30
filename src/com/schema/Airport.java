package com.schema;

public class Airport {
    int id;
    float x_position;
    float y_position;
    String name;
    String country;
    String city;

    public Airport(int id, float x_position, float y_position,
                   String name, String country, String city){
        this.id = id;
        this.x_position = x_position;
        this.y_position = y_position;
        this.name = name;
        this.country = country;
        this.city = city;
    }

    public void printAirport(){
        String arp = "Id: " + this.id + " position: (" + this.x_position + ", " + this.y_position + ") name: " + this.name
                    + " country: " + this.country + " city: " + this.city;
        System.out.println(arp);
    }

    public int getId() {
        return id;
    }

    public float getX_position() {
        return x_position;
    }

    public float getY_position() {
        return y_position;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
