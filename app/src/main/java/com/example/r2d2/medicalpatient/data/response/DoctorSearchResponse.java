package com.example.r2d2.medicalpatient.data.response;

/**
 * Created by Lollipop on 2017/5/1.
 */

public class DoctorSearchResponse {

    /**
     * status : success
     * doctor : {"id":"1","username":"zhang","name":"zhangsan","hospital":"hospital"}
     */

    private String status;
    private DoctorBean doctor;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DoctorBean getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorBean doctor) {
        this.doctor = doctor;
    }

    public static class DoctorBean {
        /**
         * id : 1
         * username : zhang
         * name : zhangsan
         * hospital : hospital
         */

        private String id;
        private String username;
        private String name;
        private String hospital;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        @Override
        public String toString() {
            return "DoctorBean{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", name='" + name + '\'' +
                    ", hospital='" + hospital + '\'' +
                    '}';
        }
    }


}
