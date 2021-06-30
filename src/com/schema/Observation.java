package com.schema;

public class Observation {
    int id;
    float height;
    float x_position;
    float y_position;
    int number_of_flight;
    String plane_name;
    String plane_id;
    int destination_airport_id;
    long time;

    public Observation(int id, float height, float x_position, float y_position,
                       int number_of_flight, String plane_name, String plane_id,
                       int destination_airport_id, long time){
        this.id = id;
        this.height = height;
        this.x_position = x_position;
        this.y_position = y_position;
        this.number_of_flight = number_of_flight;
        this.plane_name = plane_name;
        this.plane_id = plane_id;
        this.destination_airport_id = destination_airport_id;
        this.time = time;
    }

    public void printObservation(){
        String obs = "Id: " + this.id + " height: " + this.height + " position: (" + this.x_position + ", " + this.y_position
                + ") number of flight: " + this.number_of_flight + " plane name: " + this.plane_name + " plane id: " + this.plane_id
                + " destination airport id: " + this.destination_airport_id + " time: " + this.time;
        System.out.println(obs);
    }

    public int getId() {
        return id;
    }

    public float getHeight() {
        return height;
    }

    public float getX_position() {
        return x_position;
    }

    public float getY_position() {
        return y_position;
    }

    public int getNumber_of_flight() {
        return number_of_flight;
    }

    public String getPlane_name() {
        return plane_name;
    }

    public String getPlane_id() {
        return plane_id;
    }

    public int getDestination_airport_id() {
        return destination_airport_id;
    }

    public long getTime() {
        return time;
    }
}
