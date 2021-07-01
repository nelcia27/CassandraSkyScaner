package com.application;

import com.cassandra.AirportTable;
import com.cassandra.Connection;
import com.datastax.oss.driver.api.core.CqlSession;
import com.cassandra.ObservationTable;
import com.datastax.oss.driver.api.core.DriverException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.lang.Runnable;


public class Demo{

    public static boolean isAnyAlive(ArrayList<Thread> threads) {
        for(int i=0; i<threads.size(); i++) {
            if (threads.get(i).isAlive()) return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {

        Config config = new Config(Config.PROPERTIES);
        try {
            config.load();
        } catch (IOException e) {
            System.out.println("config.properties not found or invalid. Using default values.");
        }

        String keyspace = config.getKeyspace();

        Connection con = new Connection(config.getHostname(), config.getPort(), config.getRegion());
        CqlSession session = con.getSession();

        int id = 1;

        ObservationTable observationTable = new ObservationTable(keyspace, session);
        AirportTable airportTable = new AirportTable(keyspace, session);

        try {
            airportTable.insert(id, (float)100.5, (float)124.6, "TEST", "POLAND", "WARSAW");
        } catch (DriverException e) {
            System.out.println("Inserting airport failed");
        }

        List<String> planes = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i=1; i<=20; i++) {
            String plane = String.format("Plane-%d", i);
            planes.add(plane);

            Runnable driver = new Runner(id, plane, session, keyspace);
            Thread thread = new Thread(driver);
            thread.start();
            threads.add(thread);
        }
        System.out.println("HERE");

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("KONIEC");
    }
}
