package com.tara.attendanceforstudent.ui.TakeAttendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TakeAttendanceViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TakeAttendanceViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Take Attendance Report fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}