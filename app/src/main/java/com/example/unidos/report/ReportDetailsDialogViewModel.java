package com.example.unidos.report;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.Field;
import com.example.unidos.repository.Report;

public class ReportDetailsDialogViewModel extends ViewModel {
    public MutableLiveData<String> repType = new MutableLiveData<>();
    public MutableLiveData<String> elabDate = new MutableLiveData<>();
    public MutableLiveData<String> lastDate = new MutableLiveData<>();
    public MutableLiveData<String> place = new MutableLiveData<>();
    public MutableLiveData<String> clothe = new MutableLiveData<>();
    public MutableLiveData<String> details = new MutableLiveData<>();

    public void showInfo(Report report){
        Field field = new Field();
        Log.i("######", "Going to show the info");
        repType.setValue(report.repTypeToString(report.getReportType()));
        elabDate.setValue(field.dateToSimpleDate(report.getElabDate()));
        lastDate.setValue(field.dateToSimpleDate(report.getSeenDate()));
        place.setValue(report.getTown() +"\n"+ report.getAddrDetail());
        clothe.setValue(report.getClothe());
        details.setValue(report.getDetails());
    }
}
