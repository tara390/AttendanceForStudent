package com.tara.attendanceforstudent.Models;

public class TeacherModel {
    String username,password,firstname,lastname,mobileno,address,dept;

    public TeacherModel() {
    }

    public TeacherModel(String username, String password, String firstname, String lastname, String mobileno, String address, String dept) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobileno = mobileno;
        this.address = address;
        this.dept = dept;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getAddress() {
        return address;
    }

    public String getDept() {
        return dept;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
