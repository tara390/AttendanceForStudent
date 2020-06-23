package com.tara.attendanceforstudent.ui.StudentReport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudentReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StudentReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Student Report fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}