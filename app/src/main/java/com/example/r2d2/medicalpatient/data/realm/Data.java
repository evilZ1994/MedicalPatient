package com.example.r2d2.medicalpatient.data.realm;

import org.threeten.bp.LocalDateTime;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Lollipop on 2017/5/6.
 */

public class Data extends RealmObject{
    private int pressure;
    private double angle;
    private double temperature;
    private int pulsse;
    private String create_time;

    public Data(){
        super();
    }

    public Data(int pressure, double angle, double temperature, int pulsse, String create_time) {
        this.pressure = pressure;
        this.angle = angle;
        this.temperature = temperature;
        this.pulsse = pulsse;
        this.create_time = create_time;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPulsse() {
        return pulsse;
    }

    public void setPulsse(int pulsse) {
        this.pulsse = pulsse;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "Data{" +
                "pressure=" + pressure +
                ", angle=" + angle +
                ", temperature=" + temperature +
                ", pulsse=" + pulsse +
                ", create_time=" + create_time +
                '}';
    }
}
