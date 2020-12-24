package com.design.pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class Weather {
    private List<Observer> observers = new ArrayList<Observer>();
    private String weather;

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
        notifyAllObservers();
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
