package com.application;

import com.datastax.oss.driver.api.core.CqlSession;
import com.cassandra.ObservationTable;
import com.datastax.oss.driver.api.core.DriverException;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.lang.Runnable;


public class Runner implements Runnable {

    private int id;
    private String plane_name;
    CqlSession session;
    String keyspace;

    public Runner(int id, String plane_name, CqlSession session, String keyspace) {
        this.id = id;
        this.plane_name = plane_name;
        this.session = session;
        this.keyspace = keyspace;
    }

    public void run() {
        int observations = ThreadLocalRandom.current().nextInt(10, 20 + 1);

        ObservationTable observationTable = new ObservationTable(keyspace, session);

        for (int i=1; i<=observations; i++) {
            long time = new Date().getTime();
            try {
                observationTable.insert(id, (float)Math.floor(Math.random()*(100-50+1)+50), i * (float)Math.floor(Math.random()*(1000-0+1)),
                        i * (float)Math.floor(Math.random()*(1000-0+1)), i, plane_name, plane_name + "0" + i, time);
            } catch (DriverException e) {
                System.out.println("Inserting observation failed");
            }
        }
    }
}
