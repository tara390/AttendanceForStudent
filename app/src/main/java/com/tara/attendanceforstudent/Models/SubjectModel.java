package com.tara.attendanceforstudent.Models;

public class SubjectModel {
    String subjectname,department,sem;

    public SubjectModel() {
    }

    public SubjectModel(String subjectname, String department, String sem) {
        this.subjectname = subjectname;
        this.department = department;
        this.sem = sem;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public String getDepartment() {
        return department;
    }

    public String getSem() {
        return sem;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}
