package com.tp0.climagrupo2;

/**
 * Created by Samsung on 15/03/2018.
 */

public class City {

    private int id = 0;
    private String name = null;
    private String country = null;

    public City(int id, String name, String country) {
       this.id = id;
       this.name = name;
       this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFullName() {return name + ", " + country;}

}
