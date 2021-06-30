package com.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    public static final String PROPERTIES = "config.properties";

    private final String filename;
    private String hostname;
    private int port;
    private String region;
    private String keyspace;

    public Config(String filename) {
        this.filename = filename;

        // default values
        hostname = "34.123.236.101";
        port = 9042;
        region = "us-central1";
        keyspace = "skyscaner";
    }

    public void load() throws IOException {

        Properties props = new Properties();

        InputStream is = Application.class.getClassLoader().getResourceAsStream(filename);
        if (is == null) {
            throw new IOException("config file not found");
        }

        props.load(is);

        try {
            hostname = props.getProperty("hostname");
            port = Integer.parseInt(props.getProperty("port"));
            region = props.getProperty("region");
            keyspace = props.getProperty("keyspace");
        } catch (NumberFormatException e) {
            throw new IOException("config file is invalid");
        }
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getRegion() {
        return region;
    }

    public String getKeyspace() {
        return keyspace;
    }
}
