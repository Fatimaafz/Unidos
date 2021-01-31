package com.example.unidos.searching;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.ReportedPerson;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<Boolean> dismissDialog = new MutableLiveData<>();
    private MutableLiveData<List<ReportedPerson>> coincidences = new MutableLiveData<>();
    private MutableLiveData<Boolean> isInList = new MutableLiveData<>();
    private MutableLiveData<Boolean> wantToGetLocation = new MutableLiveData<>();
    private MutableLiveData<LatLng> userLocation = new MutableLiveData<>();
    private MutableLiveData<List<ReportedPerson>> coincidencesForMap = new MutableLiveData<>();
    private MutableLiveData<Boolean> test = new MutableLiveData<>();

    public MutableLiveData<Boolean> getDismissDialog() {
        Log.i("######", "Get dismiss value");
        return dismissDialog;
    }

    public void setDismissDialog(boolean hide){
        Log.i("######", "Set dismiss value");
        dismissDialog.setValue(hide);
    }

    public MutableLiveData<List<ReportedPerson>> getCoincidences() {
        Log.i("######", "Coincidences found");
        return coincidences;
    }

    public void setCoincidences(List<ReportedPerson> list) {
        coincidences.setValue(list);
        dismissDialog.setValue(true);
    }

    public MutableLiveData<Boolean> getIsInList() {
        return isInList;
    }

    public void setIsInList(boolean isInList) {
        this.isInList.setValue(isInList);
    }

    public MutableLiveData<Boolean> getWantToGetLocation() {
        return wantToGetLocation;
    }

    public void setWantToGetLocation(boolean wantToGetLocation) {
        Log.i("###### SHARED", "Want to get location");
        this.wantToGetLocation.setValue(wantToGetLocation);
    }

    public MutableLiveData<LatLng> getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation.setValue(userLocation);
    }

    public MutableLiveData<List<ReportedPerson>> getCoincidencesForMap() {
        return coincidencesForMap;
    }

    public void setCoincidencesForMap(List<ReportedPerson> coincidencesForMap) {
        Log.i("######", "I received the coinc for map");
        this.coincidencesForMap.setValue(coincidencesForMap);
    }

    public MutableLiveData<Boolean> getTest() {
        return test;
    }

    public void setTest(boolean bool) {
        test.setValue(bool);
    }
}
