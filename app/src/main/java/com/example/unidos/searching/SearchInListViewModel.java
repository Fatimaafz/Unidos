package com.example.unidos.searching;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SearchInListViewModel extends ViewModel {
    private List<ReportedPerson> reportedPersonList;
    private MutableLiveData<Boolean> isListRetrieved;
    private MutableLiveData<Boolean> showFilters;
    private MutableLiveData<Integer> opResult;

    public SearchInListViewModel(){
        isListRetrieved = new MutableLiveData<>();
        showFilters = new MutableLiveData<>();
        opResult = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsListRetrieved(){
        return isListRetrieved;
    }

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public void getRecentReports(){
        Log.i("-->><<--", "MY MVVM");

        final ReportedPersonRepository repository = new ReportedPersonRepository();
        repository.setListPeople(true);

        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        reportedPersonList = repository.getPersonList();
                        opResult.setValue(1);
                        break;
                    case -1:
                        opResult.setValue(-1);
                        break;
                    case -2:
                        opResult.setValue(-2);
                        break;
                }
            }
        });

        repository.getPersonInfo();

/*
        repository.getInfoRetrieved().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Log.i("-->><<--", "An update");
                if (Boolean.parseBoolean(((ElementoObservable) o).getElemento().toString())) {
                    reportedPersonList = repository.getPersonList();
                    isListRetrieved.setValue(true);
                }
                else
                    isListRetrieved.setValue(false);
            }
        });

        repository.getPersonInfo();*/
    }

    public List<ReportedPerson> getReportedPersonList() {
        return reportedPersonList;
    }

}
