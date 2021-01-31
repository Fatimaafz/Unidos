package com.example.unidos.searching;

import android.location.Location;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class SearchOnMapViewModel extends ViewModel {
    private ReportedPersonRepository repository;
    private List<ReportedPerson> reportedPersonList;
    private MutableLiveData<Integer> opResult = new MutableLiveData<>();

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public List<ReportedPerson> getReportedPersonList() {
        return reportedPersonList;
    }

    public void getRecentReports(LatLng userLocation){
        repository = new ReportedPersonRepository();
        repository.setListPeople(false);
        repository.setUserLocation(userLocation);
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
    }

}
