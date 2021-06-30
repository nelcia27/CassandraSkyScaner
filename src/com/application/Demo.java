package com.application;

import com.cassandra.AirportTable;
import com.cassandra.Connection;
import com.datastax.oss.driver.api.core.CqlSession;
import com.cassandra.ObservationTable;

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

    public static void main(String[] args) {

        String hostname = "34.123.236.101";
        int port = 9042;
        String region = "us-central1";
        String keyspace = "skyscaner";

        Connection con = new Connection(hostname, port, region);
        CqlSession session = con.getSession();

        int id = 1;

        ObservationTable observationTable = new ObservationTable(keyspace, session);
        AirportTable airportTable = new AirportTable(keyspace, session);
        airportTable.insert(id, (float)100.5, (float)124.6, "TEST", "POLAND", "WARSAW");

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
        while(isAnyAlive(threads)) {
            System.out.println("ALIVE");
        };

        System.out.println("KONIEC");
    }
}
