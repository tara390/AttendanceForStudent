package com.tara.attendanceforstudent.ui.ClassReport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ClassReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Class Report fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}