package com.example.unidos.access;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.accountManagement.AccountManagement;

public class MenuViewModel extends ViewModel {
    public MutableLiveData<String> curp;
    MutableLiveData <Integer> changeScreen;

    public MenuViewModel(){
        changeScreen = new MutableLiveData<>();
        curp = new MutableLiveData<>();
    }

    public void goToSearchReport(){ changeScreen.setValue(1); }

    public void goToManageAccount() {
        changeScreen.setValue(2);
    }

    public void setCurp(String curp) {
        this.curp.setValue(curp);
    }
}