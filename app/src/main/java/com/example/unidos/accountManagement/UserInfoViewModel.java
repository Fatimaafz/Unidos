package com.example.unidos.accountManagement;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.User;
import com.example.unidos.repository.UserRepository;

import java.util.Observable;
import java.util.Observer;

public class UserInfoViewModel extends ViewModel {
    private Context context;
    private User user;

    private UserRepository userRepository = new UserRepository();
    public MutableLiveData<Boolean> showLoadingElement = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> curp = new MutableLiveData<>();
    public MutableLiveData<String> genre = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> birthdate = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUserDeleted = new MutableLiveData<>();
    private MutableLiveData<Boolean> logout = new MutableLiveData<>();
    private MutableLiveData<Integer> opResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBtnPressed = new MutableLiveData<>();
    private String userCURP;

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public MutableLiveData<Boolean> getIsUserDeleted() {
        return isUserDeleted;
    }

    public MutableLiveData<Boolean> getLogout() {
        return logout;
    }

    public User getUser() {
        return user;
    }

    public void setUserCURP(String userCURP) {
        this.userCURP = userCURP;
    }

    public MutableLiveData<Boolean> getIsBtnPressed() {
        return isBtnPressed;
    }

    public void getUserInfo(){
        showLoadingElement.setValue(true);
        Log.i("@@@@ -> ", "Want to get the info");
        userRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                showLoadingElement.setValue(false);
                switch ((int)((ElementoObservable) o).getElemento()){
                    case 1:
                        user = userRepository.getUser();
                        setFieldValue();
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


        userRepository.retrieveUserData(userCURP);
    }

    public void setFieldValue(){
        if(user != null) {
            name.setValue(user.getFullName());
            curp.setValue(userCURP);
            genre.setValue(user.getSex());
            birthdate.setValue(user.getBirthDate());
            phone.setValue(user.getPhoneNumber());
        }
    }

    public void confirmAction(){
        isBtnPressed.setValue(true);
    }

    public void deleteAccount(){

        Log.i("***", "Delete account");
        userRepository.getDeleteUserStatus().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if((boolean)((ElementoObservable) o).getElemento())
                    isUserDeleted.setValue(true);
                else
                    isUserDeleted.setValue(false);
            }
        });
        userRepository.deleteUser(userCURP);
    }
    public void logout(){
        Log.i("***", "Log me out");
        logout.setValue(true);
    }
}
