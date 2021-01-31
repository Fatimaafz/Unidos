package com.example.unidos.searching;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoldViewModel extends ViewModel {
    private MutableLiveData<Boolean> showFilters;

    public MoldViewModel(){
        showFilters = new MutableLiveData<>();
    }

    public void showFilters(){
        Log.i("-->><<--", "Show filters");
        showFilters.setValue(true);
    }

    public MutableLiveData<Boolean> getShowFilters() {
        return showFilters;
    }

}
