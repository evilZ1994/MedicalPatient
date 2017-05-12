package com.example.r2d2.medicalpatient.data.response;

/**
 * Created by Lollipop on 2017/5/11.
 */

public class DoctorUserInfoResponse {
    /**
     * username : spider
     * name : jerry
     * sex : 男
     * hospital : 人民医院
     * department : 骨科
     */

    private String username;
    private String name;
    private String sex;
    private String hospital;
    private String department;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
