package com.tp0.climagrupo2;

/**
 * Created by Samsung on 17/03/2018.
 */

public class ResponseInfo {
    private String day;
    private Integer dayTemp;
    private  Integer nightTemp;
    private String dayIcon;
    private String nightIcon;

    public ResponseInfo(String day, Integer dayTemp, Integer nightTemp,String dayIcon,String nightIcon) {
        this.day = day;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
        this.dayIcon = dayIcon;
        this.nightIcon = nightIcon;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(String dayIcon) {
        this.dayIcon = dayIcon;
    }

    public String getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(String nightIcon) {
        this.nightIcon = nightIcon;
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
