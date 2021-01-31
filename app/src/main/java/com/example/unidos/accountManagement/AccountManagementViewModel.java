package com.example.unidos.accountManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.User;
//changeq
import java.util.Map;

public class AccountManagementViewModel extends ViewModel {
    private MutableLiveData<User> sharedUserInfo = new MutableLiveData<>();
    private MutableLiveData<Boolean> changeTab = new MutableLiveData<>();
    private MutableLiveData<Boolean> retrieveInfo = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBtnPressed = new MutableLiveData<>();


    public MutableLiveData<User> getSharedUserInfo() {
        return sharedUserInfo;
    }

    public void setSharedUserInfo(User userInfo) {
        sharedUserInfo.setValue(userInfo);
    }

    public void setChangeTab(Boolean change){changeTab.setValue(change);}

    public MutableLiveData<Boolean> getChangeTab(){return changeTab;}

    public void setRetrieveInfo(Boolean retrieve){retrieveInfo.setValue(retrieve);}

    public MutableLiveData<Boolean> getRetrieveInfo(){return retrieveInfo;}

    public MutableLiveData<Boolean> getIsBtnPressed() {
        return isBtnPressed;
    }

    public void setIsBtnPressed(boolean isBtnPressed) {
        this.isBtnPressed.setValue(isBtnPressed);
    }
}
