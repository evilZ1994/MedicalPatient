package com.example.r2d2.medicalpatient.data.response;

/**
 * Created by Lollipop on 2017/4/28.
 */

public class LoginResponse {
    /**
     * status : success
     * message : null
     * user : {"username":"zhangsan","password":"1234","name":"zhangsan","doctor_id":"1"}
     */

    private String status;
    private String message;
    private UserBean user;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", user=" + user +
                '}';
    }

    public static class UserBean {
        /**
         * id : 1
         * username : zhangsan
         * password : 1234
         * name : zhangsan
         * doctor_id : 1
         */
        private int id;
        private String username;
        private String password;
        private String name;
        private String doctor_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDoctor_id() {
            return doctor_id;
        }

        public void setDoctor_id(String doctor_id) {
            this.doctor_id = doctor_id;
        }

        @Override
        public String toString() {
            return "UserBean{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", name='" + name + '\'' +
                    ", doctor_id='" + doctor_id + '\'' +
                    '}';
        }
    }
}
