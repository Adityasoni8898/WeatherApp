package com.example.weather.Models;

public class Current {
    private String feelslike_c;
    private int temp_c;
    private int humidity;
    private float uv;
    Condition condition;

    public Current(String feelslike_c, int temp_c, int humidity, float uv, Condition condition) {
        this.feelslike_c = feelslike_c;
        this.temp_c = temp_c;
        this.humidity = humidity;
        this.uv = uv;
        this.condition = condition;
    }

    public String getFeelslike_c() {
        return feelslike_c;
    }

    public void setFeelslike_c(String feelslike_c) {
        this.feelslike_c = feelslike_c;
    }

    public int getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(int temp_c) {
        this.temp_c = temp_c;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getUv() {
        return uv;
    }

    public void setUv(float uv) {
        this.uv = uv;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Current{" +
                "feelslike_c='" + feelslike_c + '\'' +
                ", temp_c=" + temp_c +
                ", humidity=" + humidity +
                ", uv=" + uv +
                ", condition=" + condition +
                '}';
    }
}
