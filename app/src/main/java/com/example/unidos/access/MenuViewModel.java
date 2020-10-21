package com.example.unidos.access;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.accountManagement.AccountManagement;

public class MenuViewModel extends ViewModel {
    MutableLiveData <Boolean> isPressed = new MutableLiveData<>();
    Context menuContext;

    public MenuViewModel(){}

    public void setMenuContext(Context menuContext) {
        this.menuContext = menuContext;
    }

    private Context getMenuContext(){
        return menuContext;
    }

    public void goToManageAccount() {
        Intent intent = new Intent(getMenuContext(), AccountManagement.class);
        getMenuContext().startActivity(intent);
    }
}