package com.hcmus.manage_employee.ui.calendar;

import androidx.lifecycle.ViewModel;

import com.hcmus.manage_employee.models.TaskModel;

import java.util.ArrayList;

public class CalendarViewModel extends ViewModel {

    private ArrayList<TaskModel> arrayListMutableLiveData;
    public CalendarViewModel() {
        arrayListMutableLiveData = new ArrayList<>();
        getData();
    }

    public ArrayList<TaskModel> getArrayListMutableLiveData() {
        return arrayListMutableLiveData;
    }

    public void getData() {

    }
}