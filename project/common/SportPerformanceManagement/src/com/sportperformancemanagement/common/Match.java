package com.sportperformancemanagement.common;

/**
 * Created by joostouwerling on 24/02/16.
 */
public class Match {

    private String name;
    private int id;
    private String server;
    private int port;

    public Match(String n, int id, String server, int port)
    {
        setName(n);
        setId(id);
        setServer(server);
        setPort(port);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
