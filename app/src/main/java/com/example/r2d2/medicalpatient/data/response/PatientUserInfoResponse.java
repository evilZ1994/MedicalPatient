package com.example.r2d2.medicalpatient.data.response;

/**
 * Created by Lollipop on 2017/5/11.
 */

public class PatientUserInfoResponse {
    /**
     * username : spider
     * name : zhangsan
     * sex : 男
     * age : 10
     */

    private String username;
    private String name;
    private String sex;
    private String age;

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
