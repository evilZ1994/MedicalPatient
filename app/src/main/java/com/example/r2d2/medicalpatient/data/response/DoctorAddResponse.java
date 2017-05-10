package com.example.r2d2.medicalpatient.data.response;

/**
 * Created by Lollipop on 2017/5/2.
 */

public class DoctorAddResponse {

    /**
     * status : success
     * doctor_id : 1
     * doctor_name : zhangsan
     */

    private String status;
    private int doctor_id;
    private String doctor_name;
    private String doctor_username;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getDoctor_username() {
        return doctor_username;
    }

    public void setDoctor_username(String doctor_username) {
        this.doctor_username = doctor_username;
    }
}
