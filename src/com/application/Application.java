package com.application;

import com.datastax.oss.driver.api.core.CqlSession;
import com.cassandra.Connection;
import com.cassandra.AirportTable;
import com.cassandra.ObservationTable;
import com.schema.Airport;
import com.schema.Observation;
import jnr.ffi.annotations.In;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.InterruptedException;

public class Application{

    private ObservationTable observationTable;
    private AirportTable airportTable;

    public Application(String hostname, int port, String region, String keyspace) {
        Connection connection = new Connection(hostname, port, region);
        CqlSession session = connection.getSession();

        this.observationTable = new ObservationTable(keyspace, session);
        this.airportTable = new AirportTable(keyspace, session);

        observationTable.create();
        airportTable.create();


    }

    public void appInterface() throws IOException {
        while (true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            String[] info = line.split(" ");
            if(info[0].equals("insert")){
                if(info[1].equals("observation")){
                    observationTable.insert(Integer.parseInt(info[2]), Float.parseFloat(info[3]),
                            Float.parseFloat(info[4]), Float.parseFloat(info[5]), Integer.parseInt(info[6]),
                            info[7], info[8], Long.parseLong(info[9]));
                }else if(info[1].equals("airport")){
                    airportTable.insert(Integer.parseInt(info[2]), Float.parseFloat(info[3]),
                            Float.parseFloat(info[4]), info[5], info[6], info[7]);
                }
            }else if(info[0].equals("info")){
                if(info[1].equals("airport")){
                    Airport airport = airportTable.select_airport(Integer.parseInt(info[2]));
                    airport.printAirport();
                }else if(info[1].equals("observation_flight")){
                    List<Observation> observations = observationTable.select_flight(Integer.parseInt(info[2]));
                    for(Observation obs : observations){
                        obs.printObservation();
                    }
                }else if(info[1].equals("observation_plane")){
                    List<Observation> observations = observationTable.select_plane(info[2]);
                    for(Observation obs : observations){
                        obs.printObservation();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        String hostname = "34.123.236.101";
        int port = 9042;
        String region = "us-central1";
        String keyspace = "skyscaner";

        Application app = new Application(hostname, port, region, keyspace);

        app.appInterface();

    }
}
