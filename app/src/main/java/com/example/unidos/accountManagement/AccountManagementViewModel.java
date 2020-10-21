package com.example.unidos.accountManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.User;
//changeq
import java.util.Map;

public class AccountManagementViewModel extends ViewModel {
    private MutableLiveData<Map<String, Object>> sharedText = new MutableLiveData<>();
    private MutableLiveData<User> sharedUserInfo = new MutableLiveData<>();
    private MutableLiveData<Boolean> wasInfoUpdated = new MutableLiveData<>();
    private MutableLiveData<Boolean> changeTab = new MutableLiveData<>();
    private MutableLiveData<Boolean> retrieveInfo = new MutableLiveData<>();

    public LiveData<Map<String, Object>> getSharedText() {
        return sharedText;
    }

    public void setSharedText(Map<String, Object> map) {
        sharedText.setValue(map);
    }

    public MutableLiveData<User> getSharedUserInfo() {
        return sharedUserInfo;
    }

    public void setSharedUserInfo(User userInfo) {
        sharedUserInfo.setValue(userInfo);
    }

    public MutableLiveData<Boolean> getWasInfoUpdated(){return wasInfoUpdated;}

    public void setWasInfoUpdated(Boolean wasUpdated){wasInfoUpdated.setValue(wasUpdated);}

    public void setChangeTab(Boolean change){changeTab.setValue(change);}

    public MutableLiveData<Boolean> getChangeTab(){return changeTab;}

    public void setRetrieveInfo(Boolean retrieve){retrieveInfo.setValue(retrieve);}

    public MutableLiveData<Boolean> getRetrieveInfo(){return retrieveInfo;} //hola

}
