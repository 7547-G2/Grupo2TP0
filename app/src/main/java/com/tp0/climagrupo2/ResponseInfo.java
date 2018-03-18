package com.tp0.climagrupo2;

/**
 * Created by Samsung on 17/03/2018.
 */

public class ResponseInfo {
    private String day;
    private Integer dayTemp;
    private  Integer nightTemp;

    public ResponseInfo(String day, Integer dayTemp, Integer nightTemp) {
        this.day = day;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(Integer dayTemp) {
        this.dayTemp = dayTemp;
    }

    public Integer getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(Integer nightTemp) {
        this.nightTemp = nightTemp;
    }
}
