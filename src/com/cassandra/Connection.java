package com.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


import java.net.InetSocketAddress;

public class Connection {

    private CqlSession session;
    private String hostname;
    private int port;
    private String region;

    public Connection(String hostname, int port, String region){
        this.hostname = hostname;
        this.port = port;
        this.region = region;
        createSession();
    }

    private void createSession(){
        CqlSessionBuilder builder = CqlSession.builder();
        builder.addContactPoint(new InetSocketAddress(hostname, port));
        builder.withLocalDatacenter(region);
        session = builder.build();
    }

    public CqlSession getSession() {
        return session;
    }

}
