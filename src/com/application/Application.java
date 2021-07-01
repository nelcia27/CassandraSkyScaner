package com.application;

import com.datastax.oss.driver.api.core.CqlSession;
import com.cassandra.Connection;
import com.cassandra.AirportTable;
import com.cassandra.ObservationTable;
import com.schema.Airport;
import com.schema.Observation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Application{

    private final ObservationTable observationTable;
    private final AirportTable airportTable;
    private final CqlSession session;

    public Application(String hostname, int port, String region, String keyspace) {
        Connection connection = new Connection(hostname, port, region);
        session = connection.getSession();

        this.observationTable = new ObservationTable(keyspace, session);
        this.airportTable = new AirportTable(keyspace, session);

        observationTable.create();
        airportTable.create();
    }

    // Returns whether execution should continue
    // or throw ArrayOutOfBoundException or NumberFormatException
    public boolean parseCommand(String[] info) {
        if (info[0].equals("insert")){
            if(info[1].equals("observation")){
                observationTable.insert(Integer.parseInt(info[2]), Float.parseFloat(info[3]),
                        Float.parseFloat(info[4]), Float.parseFloat(info[5]), Integer.parseInt(info[6]),
                        info[7], info[8], Long.parseLong(info[9]));
            }else if(info[1].equals("airport")){
                airportTable.insert(Integer.parseInt(info[2]), Float.parseFloat(info[3]),
                        Float.parseFloat(info[4]), info[5], info[6], info[7]);
            }
        }else if(info[0].equals("info")){
            if (info[1].equals("airport")){
                Airport airport = airportTable.select_airport(Integer.parseInt(info[2]));
                if (airport == null) {
                    System.out.println("Airport with this id does not exist");
                    return true;
                }

                airport.printAirport();
            } else if(info[1].equals("observation_flight")){
                List<Observation> observations = observationTable.select_flight(Integer.parseInt(info[2]));
                if (observations.size() == 0) {
                    System.out.println("No observations for this flight number");
                    return true;
                }

                for(Observation obs : observations){
                    obs.printObservation();
                }
            }else if(info[1].equals("observation_plane")){
                List<Observation> observations = observationTable.select_plane(info[2]);
                if (observations.size() == 0) {
                    System.out.println("No observations for this plane");
                }

                for(Observation obs : observations){
                    obs.printObservation();
                }
            }
        } else if (info[0].equals("exit")) {
            return false;
        }
        return true;
    }

    public void appInterface() throws IOException {
        while (true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            String[] info = line.split(" ");

            try {
                boolean shouldContinue = parseCommand(info);
                if (!shouldContinue) {
                    reader.close();
                    session.close();
                    return;
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Invalid command");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Config config = new Config(Config.PROPERTIES);
        try {
            config.load();
        } catch (IOException e) {
            System.out.println("config.properties not found or invalid. Using default values.");
        }

        Application app = new Application(config.getHostname(), config.getPort(), config.getRegion(), config.getKeyspace());

        app.appInterface();

    }
}
