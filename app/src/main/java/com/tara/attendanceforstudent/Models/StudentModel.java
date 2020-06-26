package com.tara.attendanceforstudent.Models;

public class StudentModel {
    String firstname, lastname, mobile, address, fingerprint, parentmobile, email, dept, div, sem;


    public StudentModel() {
    }

    public StudentModel(String firstname, String lastname, String mobile, String address, String fingerprint, String parentmobile, String email, String dept, String div, String sem) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobile = mobile;
        this.address = address;
        this.fingerprint = fingerprint;
        this.parentmobile = parentmobile;
        this.email = email;
        this.dept = dept;
        this.div = div;
        this.sem = sem;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getParentmobile() {
        return parentmobile;
    }

    public String getEmail() {
        return email;
    }

    public String getDept() {
        return dept;
    }

    public String getDiv() {
        return div;
    }

    public String getSem() {
        return sem;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public void setParentmobile(String parentmobile) {
        this.parentmobile = parentmobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}
