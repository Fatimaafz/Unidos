package com.example.unidos.report;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.Report;
import com.example.unidos.repository.ReportedPerson;

import java.util.Date;

public class ReportSharedViewModel extends ViewModel {
    protected MutableLiveData<ReportedPerson> personReceived;
    protected MutableLiveData<String> personID;
    private MutableLiveData<Report> report;
    private MutableLiveData<Boolean> repGenerated;
    private MutableLiveData<Boolean> isFound;
    private MutableLiveData<Boolean> changeTab;
    private Date firstSeenDate;

    public ReportSharedViewModel(){
        personReceived = new MutableLiveData<>();
        personID = new MutableLiveData<>();
        report = new MutableLiveData<>();
        repGenerated = new MutableLiveData<>();
        isFound = new MutableLiveData<>();
        changeTab = new MutableLiveData<>();
    }

    public MutableLiveData<ReportedPerson> getPersonReceived() {
        return personReceived;
    }

    public Date getFirstSeenDate() {
        return firstSeenDate;
    }

    public void setPersonReceived(ReportedPerson person) {
        personReceived.setValue(person);
        personID.setValue(person.getCurp());
        firstSeenDate = person.getFirstSeenDate();
        isFound.setValue(person.isFound());
    }

    public MutableLiveData<String> getPersonID() {
        return personID;
    }

    private void setPersonID(MutableLiveData<String> personID) {
        this.personID = personID;
    }

    public MutableLiveData<Report> getReport() {
        return report;
    }

    public void setReport(Report r) {
        report.setValue(r);
    }

    public MutableLiveData<Boolean> getRepGenerated() {
        return repGenerated;
    }

    public void setRepGenerated(boolean isGenerated) {
        repGenerated.setValue(isGenerated);
    }

    public MutableLiveData<Boolean> getIsFound() {
        return isFound;
    }

    public MutableLiveData<Boolean> getChangeTab() {
        return changeTab;
    }

    public void setChangeTab(boolean change) {
        changeTab.setValue(change);
    }
}
