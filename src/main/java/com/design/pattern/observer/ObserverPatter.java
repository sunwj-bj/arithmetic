package com.design.pattern.observer;

public class ObserverPatter {
    public static void main(String[] args) {
        Weather weather = new Weather();
        CarObserver carObserver = new CarObserver(weather);
        HouseObserver houseObserver = new HouseObserver(weather);
        weather.setWeather("rain");

    }
}
