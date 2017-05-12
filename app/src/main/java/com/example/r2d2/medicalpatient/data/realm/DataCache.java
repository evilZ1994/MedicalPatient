package com.example.r2d2.medicalpatient.data.realm;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by Lollipop on 2017/5/7.
 */
@RealmClass
public class DataCache implements RealmModel{
    private int pressure;
    private double angle;
    private double temperature;
    private int pulse;
    private String create_time;
    private int patient_id;

    public DataCache(){
        super();
    }

    public DataCache(int pressure, double angle, double temperature, int pulse, String create_time, int patient_id) {
        this.pressure = pressure;
        this.angle = angle;
        this.temperature = temperature;
        this.pulse = pulse;
        this.create_time = create_time;
        this.patient_id = patient_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
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

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
