package com.example.direction2;

import java.util.ArrayList;
import java.util.List;

public class Vert implements Comparable<Vert> {

    private boolean visited;
    private String name;
    private double lat;
    private double lng;
    private java.util.List<Edge> List;
    private double dist = Double.MAX_VALUE;
    private Vert pr;

    public Vert() {
    }

    public Vert(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.List = new ArrayList<>();
    }

    public List<Edge> getList() {
        return List;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setList(List<Edge> List) {
        this.List = List;
    }

    public void addNeighbour(Edge edge) {
        this.List.add(edge);
    }

    public boolean Visited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Vert getPr() {
        return pr;
    }

    public void setPr(Vert pr) {
        this.pr = pr;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Vert otherV) {
        return Double.compare(this.dist, otherV.getDist());
    }
}