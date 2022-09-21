package com.example.weather.Models;

public class WeatherData {
    Location location;
    Current current;

    public WeatherData(Location location, Current current) {
        this.location = location;
        this.current = current;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "location=" + location +
                ", current=" + current +
                '}';
    }
}
