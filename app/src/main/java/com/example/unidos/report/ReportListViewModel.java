package com.example.unidos.report;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.Report;
import com.example.unidos.repository.ReportRepository;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ReportListViewModel extends ViewModel {
    private MutableLiveData<Integer> opResult;
    private List<Report> reportList;

    public ReportListViewModel(){
        opResult = new MutableLiveData<>();
    }

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void searchReports(String personID, Date firstSeenDate){
        final ReportRepository repository = new ReportRepository();
        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int)((ElementoObservable)o).getElemento()){
                    case 1:
                        reportList = repository.getReportList();
                        opResult.setValue(1);
                        break;
                    case -1:
                        opResult.setValue(-1);
                        break;
                    case -2:
                        opResult.setValue(-2);
                }
            }
        });

        repository.findReports(personID, firstSeenDate);
    }
}
