package com.example.unidos.accountManagement;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.User;
import com.example.unidos.UserRepository;
import com.example.unidos.VerifyPhoneActivity;
import com.example.unidos.shared.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class EditUserInfoViewModel extends ViewModel {
    private Context context;
    private User user = new User();
    private UserRepository userRepository = new UserRepository();
    private VerifyPhoneActivity verifyPhoneActivity = new VerifyPhoneActivity();
    private String initialUsrInfo = "";
    private Map<String, Object> map = new HashMap<>();
    private int numberOfField;
    private String errorMessage = "";
    private boolean bool, hasName2, hasLastName2;
    private Field field = new Field();
    private List<Boolean> fieldState = new ArrayList<Boolean>(
            Arrays.asList(true, true, true, true, true, true, true)
    );

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> name2 = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> lastName2 = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();
    public MutableLiveData<String> sex = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> curp = new MutableLiveData<>();
    public MutableLiveData<Boolean> buttonEnabled = new MutableLiveData<>();
    public MutableLiveData<Integer> fieldError = new MutableLiveData<>();
    public MutableLiveData<Integer> validField = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFieldDateSelected = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFieldSexSelected = new MutableLiveData<>();
    public MutableLiveData<String> opResult = new MutableLiveData<>();

    //changeq
    public void setUser(User user){
        this.user = user;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setNumberOfField(int numberOfField) {
        this.numberOfField = numberOfField;
    }

    public void setContext(Context context){
        this.context = context;
    }

    protected void setFieldValue(){
        Log.i("@@@@ ->>>", "Going to set field values");
        name.setValue(user.getFirstName());

        if(user.getSecondName() != null)
            name2.setValue(user.getSecondName());
        else
            name2.setValue("");

        lastName.setValue(user.getSurname1());

        if(user.getSurname2() != null)
            lastName2.setValue(user.getSurname2());
        else
            lastName2.setValue("");

        birthDate.setValue(user.getDateBirth());
        sex.setValue(user.getSex());
        phone.setValue(user.getPhoneNumber());
        curp.setValue(user.getCURP());

        initialUsrInfo = user.getInitialUserInfo();
    }

    public void setIsFieldDateSelected() {
        numberOfField = 4;
        try {
            birthDate.getValue().isEmpty();
        }catch (NullPointerException e){
            errorMessage = "empty";
            fieldError.setValue(numberOfField);
        }
        isFieldDateSelected.setValue(true);
    }

    public void setIsFieldSexSelected(){
        numberOfField = 5;
        try {
            sex.getValue().isEmpty();
        }catch (NullPointerException e){
            errorMessage = "empty";
            fieldError.setValue(numberOfField);
        }
        isFieldSexSelected.setValue(true);
    }

    public void checkSex(String selection){
        numberOfField=5;
        sex.setValue(selection);
        bool = field.isFieldEmpty(selection);
        fieldState.set(numberOfField, !bool);
        sendVerifResult(bool);
    }

    public void checkDate(String date){
        numberOfField = 4;
        birthDate.setValue(date);
        bool = field.isFieldEmpty(date);
        fieldState.set(numberOfField, !bool);
        sendVerifResult(bool);
    }

    protected void validateName(String s){
        field.setNumberOfField(numberOfField);
        bool = field.isNameWrong(s);
        fieldState.set(numberOfField, !bool);
        sendVerifResult(bool);
    }

    protected void validatePhone() {
        numberOfField=6;
        bool = field.isPhoneWrong(phone.getValue());
        fieldState.set(numberOfField, !bool);
        sendVerifResult(bool);
    }

    protected void checkListFieldState(){
        if (field.checkFieldStatus(fieldState))
            buttonEnabled.setValue(true);
        else
            buttonEnabled.setValue(false);
    }

    private void sendVerifResult(Boolean b){
        if (b) {
            errorMessage = field.getValidationMsg();
            buttonEnabled.setValue(false);
            fieldError.setValue(numberOfField);
        }else {
            validField.setValue(numberOfField);
            checkListFieldState();
        }
    }

    public void saveChanges() {
        Log.i("°°° CURP", curp.getValue());
        if (checkInfoMatch())
            opResult.setValue("noChange");
        else{
            if(checkInternetConnection()) {
                map.put("curp", curp.getValue());
                if (!phone.getValue().equals(user.getPhoneNumber())) { /** Must do the phone autoverification **/
                    userRepository.getPhoneExistence().addObserver(new Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            int result = Integer.parseInt(((ElementoObservable) o).getElemento().toString());
                            if (result == -1)
                                opResult.setValue("phoneFound");
                            else if (result == -2)
                                opResult.setValue("fail");
                            else if (result == 1) {
                                verifyPhoneActivity.getVerificationStatusObservable().addObserver(new Observer() {
                                    @Override
                                    public void update(Observable o, Object arg) {
                                        if (Integer.parseInt(((ElementoObservable) o).getElemento().toString()) == 1) {
                                            fillMap();
                                            map.put("phoneNumber", phone.getValue());
                                            updateUserInfo();
                                        } else {
                                            Log.i("@@@@ ", "-2");
                                            opResult.setValue("phoneVerifFail");
                                        }
                                    }
                                });

                                verifyPhoneActivity.getPhoneVerifTimeOut().addObserver(new Observer() {
                                    @Override
                                    public void update(Observable o, Object arg) {
                                        if (Boolean.parseBoolean(((ElementoObservable) o).getElemento().toString()))
                                            opResult.setValue("phoneVerifFail");
                                    }
                                });
                                Log.i("@@@@ Phone", phone.getValue());
                                verifyPhoneActivity.sendVerificationMethod(phone.getValue());
                            }
                        }
                    });
                    userRepository.readPhone(phone.getValue());

                } else {
                    fillMap();
                    map.put("phoneNumber", phone.getValue());
                    updateUserInfo();
                }
            }
        }
    }

    private void updateUserInfo(){
        userRepository.getUserUpdateObservable().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(Boolean.parseBoolean(((ElementoObservable)o).getElemento().toString()))
                    opResult.setValue("update");
                else
                    opResult.setValue("fail");
            }
        });
        userRepository.updateUserInfo(map);
    }

    private Boolean checkInternetConnection(){
        Connection connection = new Connection(context);

        /** If the phone hasn´t connection**/
        if(connection.isNotConnected()) {
            /** Inform the observer it must show an error message
             and hide the progress bar. */
            opResult.setValue("noConn");
            return false;
        }else if(connection.checkConnection()){
            /** Is the connection stable?**/
            System.out.println("Sí cuenta con la calidad deseada");
            /** Continue with the register. **/
            return true;
        } else{
            /**Probably the connection is unstable. **/
            /**Inform the observer it must show the progress bar
             * and display an error message. */
            opResult.setValue("badConn");
            return false;
        }
    }

    private void fillMap(){
        map.put("firstName", name.getValue());

        if(hasName2) {
            map.put("secondName", name2.getValue());
        }

        map.put("surname1", lastName.getValue());

        if(hasLastName2) {
            map.put("surname2", lastName2.getValue());
        }

        map.put("dateBirth", birthDate.getValue());

        map.put("sex", sex.getValue());
    }

    public String getFormValues(){
        String changedInfo = name.getValue()+lastName.getValue()+birthDate.getValue()
                + sex.getValue() + phone.getValue();
        if(name2.getValue() != null){
            if(!name2.getValue().isEmpty()) {
                changedInfo += name2.getValue();
                hasName2 = true;
            }
        }

        if(lastName2.getValue() != null){
            if(!lastName2.getValue().isEmpty()) {
                changedInfo += lastName2.getValue();
                hasLastName2 = true;
            }
        }

        Log.i("°°° Old info", initialUsrInfo);
        Log.i("°°° Current info", changedInfo);

        return changedInfo;
    }

    public boolean checkInfoMatch(){
        if(initialUsrInfo.equals(getFormValues()))
            return true;
        else return false;
    }
}

