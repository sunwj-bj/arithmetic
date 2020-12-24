package com.design.pattern.observer;

public class CarObserver extends Observer {

    public CarObserver(Weather weather) {
        this.weather=weather;
        weather.addObserver(this);
    }

    @Override
    public void update() {
        if ("rain".equals(weather.getWeather())){
            System.out.println("下雨了，汽车关闭天窗和侧窗！");
        }else {
            System.out.println("没有下雨，汽车可以通风！");
        }
    }
}
