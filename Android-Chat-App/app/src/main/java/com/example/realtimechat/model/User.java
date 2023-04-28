package com.example.realtimechat.model;

public class User {
    String address;
    String dob;
    String email;
    String feedback;
    String gender;
    String job;
    String name;
    String password;
    String phone;
    String state;
    Boolean topStudent;
    String workplace;

    public User() {
    }

    public User(String address, String dob, String email, String feedback, String gender, String job, String name, String password, String phone, String state, Boolean topStudent, String workplace) {
        this.address = address;
        this.dob = dob;
        this.email = email;
        this.feedback = feedback;
        this.gender = gender;
        this.job = job;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.state = state;
        this.topStudent = topStudent;
        this.workplace = workplace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getTopStudent() {
        return topStudent;
    }

    public void setTopStudent(Boolean topStudent) {
        this.topStudent = topStudent;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }
}
