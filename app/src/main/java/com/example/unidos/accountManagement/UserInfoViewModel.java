package com.example.unidos.accountManagement;
//changeq

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.PersistentData;
import com.example.unidos.User;
import com.example.unidos.UserRepository;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class UserInfoViewModel extends ViewModel {
    private Context context;
    private View view;
    private Messages messages;
    private PersistentData persistentData;
    private User user;

    private UserRepository userRepository = new UserRepository();
    private AccountManagementViewModel accountManagementViewModel;
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> curp = new MutableLiveData<>();
    public MutableLiveData<String> genre = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> birthdate = new MutableLiveData<>();
    public MutableLiveData<Boolean> infoExist = new MutableLiveData<>();
    private MutableLiveData<String> userInfoResponse = new MutableLiveData<>();
    public MutableLiveData<Map<String, Object>> mapMutableLiveData = new MutableLiveData<>();


    public MutableLiveData<String> getUserInfoResponse() {
        return userInfoResponse;
    }

    private void setUserInfoResponse(String error) {
        userInfoResponse.setValue(error);
    }

    public void setContext(Context context) {
        this.context = context;
        messages = new Messages();
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setAccountManagementViewModel(AccountManagementViewModel accountManagementViewModel) {
        this.accountManagementViewModel = accountManagementViewModel;
    }

    private String retrieveUserCURP(){
        persistentData = new PersistentData();
        return persistentData.getCURPValue(context);
    }

    private boolean checkPhoneConnection(){
        Connection connection = new Connection(context);
        if (connection.isNotConnected()) {
            /** The observer will display a message **/
            setUserInfoResponse("noConn");
            return false;
        } else if (connection.checkConnection()) {
            /** In case the mobile has an stable
             * Internet connection. */
            System.out.println("Sí cuenta con la calidad deseada");
            return true;
        } else { /** Probably the connection is unstable. **/
            /** The observer will detect the change in the value and
             * then it will show an error message. */
            setUserInfoResponse("badConn");
            System.out.println("No cuenta con la calidad deseada");
            return false;
        }
    }

    public void getUserInfo(){
        Log.i("@@@@ -> ", "Want to get the info");

        if(checkPhoneConnection()){
            userRepository.userInfoExist.addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    if(Integer.parseInt(((ElementoObservable) o).getElemento().toString()) == 1) {
                        user = userRepository.getUser();
                        accountManagementViewModel.setSharedUserInfo(user);
                        setFieldValue();
                        setUserInfoResponse("success");
                    } else
                        setUserInfoResponse("fail");
                }
            });
            /*setUserInfoResponse("success");
            User u = new User();
            u.setFirstName("Tronquito");
            u.setSurname1("Pérez");
            u.setPhoneNumber("5567876945");
            u.setCURP(retrieveUserCURP());
            u.setSex("Mujer");
            u.setDateBirth("20/07/1998");
            user = u;
            setFieldValue();*/

            userRepository.retrieveUserData(retrieveUserCURP());
        }
        else
            accountManagementViewModel.setSharedUserInfo(null);

    }

    public void setFieldValue(){
        if(user != null) {
            accountManagementViewModel.setSharedUserInfo(user);
            name.setValue(user.getFullName());
            curp.setValue(retrieveUserCURP());
            genre.setValue(user.getSex());
            birthdate.setValue(user.getDateBirth());
            phone.setValue(user.getPhoneNumber());
        }
    }

    public void deleteAccount(){

        Log.i("***", "Delete account");
        final String curp = retrieveUserCURP();
        userRepository.getDeleteUserStatus().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(Boolean.parseBoolean(((ElementoObservable) o).getElemento().toString())){
                    persistentData = new PersistentData(context);
                    persistentData.removePreferences();
                    setUserInfoResponse("deleted");
                }else
                    setUserInfoResponse("failDelete");
            }
        });
        userRepository.deleteUser(curp);
    }
    public void logout(){
        Log.i("***", "Log me out");
        persistentData = new PersistentData(context);
        persistentData.removePreferences();
    }
}
