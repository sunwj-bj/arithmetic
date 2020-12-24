package com.design.pattern.observer;

public class HouseObserver extends Observer {
    public HouseObserver(Weather weather) {
        this.weather=weather;
        weather.addObserver(this);
    }

    @Override
    public void update() {
        if ("rain".equals(weather.getWeather())){
            System.out.println("下雨了，关闭门窗！");
        }else{
            System.out.println("未下雨，开始室内通风！");
        }
    }
}
