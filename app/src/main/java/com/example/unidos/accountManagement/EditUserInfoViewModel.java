package com.example.unidos.accountManagement;

import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.FieldConstant;
import com.example.unidos.repository.ReportUserRepository;
import com.example.unidos.repository.User;
import com.example.unidos.repository.UserRepository;
import com.example.unidos.VerifyPhoneActivity;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class EditUserInfoViewModel extends ViewModel {
    private User user = new User();
    private UserRepository userRepository = new UserRepository();
    private String initialUsrInfo = "";
    private Map<String, Object> map;
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> name2 = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> lastName2 = new MutableLiveData<>();
    public MutableLiveData<String> birthDate = new MutableLiveData<>();
    public MutableLiveData<String> sex = new MutableLiveData<>();
    public MutableLiveData<String> phone = new MutableLiveData<>();
    public MutableLiveData<String> curp = new MutableLiveData<>();
    public MutableLiveData<Boolean> buttonEnabled = new MutableLiveData<>();
    private MutableLiveData<Integer> opResult = new MutableLiveData<>();
    private com.example.unidos.repository.Field repoField = new com.example.unidos.repository.Field();
    private MutableLiveData<Integer> numField = new MutableLiveData<>();
    private int error;
    private MutableLiveData<Boolean> configCalendar = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkConnection = new MutableLiveData<>();
    private Map<String, Object> dataForReport;
    private List<Boolean> fieldStatus = new ArrayList<Boolean>(
            Arrays.asList(true, true, true, true, true, true, true, false)
    );

    private String nameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)+$";
    private String secondNameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)*$";
    private String phoneRE="[0-9]{10}";
    protected MutableLiveData<Boolean> onMenuClick = new MutableLiveData<>();

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public MutableLiveData<Boolean> getCheckConnection() {
        return checkConnection;
    }

    public MutableLiveData<Boolean> getConfigCalendar() {
        return configCalendar;
    }

    public void setBirthDate(String date) {
        birthDate.setValue(date);
    }

    public MutableLiveData<Integer> getNumField() {
        return numField;
    }

    public int getError(){
        return error;
    }

    public void displayMenu(){
        onMenuClick.setValue(true);
    }

    public void setUser(User user){
        if(user.getName() != null)
            fieldStatus.set(fieldStatus.size()-1, true);
        this.user = user;
    }

    protected void setFieldValue(){
        name.setValue(user.getName());
        name2.setValue(user.getName2());
        lastName.setValue(user.getSurname());
        lastName2.setValue(user.getSurname2());
        birthDate.setValue(user.getBirthDate());
        sex.setValue(user.getSex());
        phone.setValue(user.getPhoneNumber());
        curp.setValue(user.getCURP());
        initialUsrInfo = user.getInitialUserInfo();
    }

    protected void validateName(int num, String value){
        if(num == 1 || num == 3)
            error = repoField.validateNonObligatoryField(value, secondNameRE);
        else
            error = repoField.validateObligatoryField(value, nameRE);
        fieldStatus.set(num, repoField.enableFlag(error));
        buttonEnabled.setValue(repoField.checkFieldStatus(fieldStatus, fieldStatus.get(num)));
        numField.setValue(num);
    }

    protected void validatePhone() {
        error = repoField.validateObligatoryField(phone.getValue(), phoneRE);
        fieldStatus.set(6, repoField.enableFlag(error));
        buttonEnabled.setValue(repoField.checkFieldStatus(fieldStatus, fieldStatus.get(6)));
        numField.setValue(6);
    }

    protected void checkSex(){
        error = repoField.validateObligatoryField(sex.getValue(), "[a-zA-Z]+$");
        fieldStatus.set(5, repoField.enableFlag(error));
        buttonEnabled.setValue(repoField.checkFieldStatus(fieldStatus, fieldStatus.get(5)));
        numField.setValue(5);
    }

    protected void checkDate(){
        error = repoField.validateObligatoryField(birthDate.getValue(), "[0-9]{2}/[0-9]{2}/[0-9]{4}");
        fieldStatus.set(4, repoField.enableFlag(error));
        buttonEnabled.setValue(repoField.checkFieldStatus(fieldStatus, fieldStatus.get(4)));
        numField.setValue(4);
    }

    public void showCalendar(){
        checkDate();
        configCalendar.setValue(true);
    }

    public boolean checkInfoMatch(){
        String changedInfo = name.getValue()+ lastName.getValue()+ birthDate.getValue()
                + sex.getValue() + phone.getValue();

        if(repoField.isFieldEmpty(name2.getValue()))
            changedInfo += null;
        else
            changedInfo += name2.getValue();

        if (repoField.isFieldEmpty(lastName2.getValue()))
            changedInfo += null;
        else changedInfo += lastName2.getValue();

        if(!initialUsrInfo.equalsIgnoreCase(changedInfo))
            return false;
        else return true;
    }

    private void checkValue(String fieldKey, String newValue, String originalValue, boolean updateInUser) {
        String value;
        if(!repoField.isFieldEmpty(newValue)){
            if(!newValue.equalsIgnoreCase(originalValue)){
                if(!fieldKey.equals(FieldConstant.FIELD_BDAY_ID))
                    value=(Character.toUpperCase(newValue.charAt(0)) + newValue.substring(1).toLowerCase()).trim();
                else
                    value = newValue.trim();
                if (!updateInUser) dataForReport.put(fieldKey, value);
                map.put(fieldKey, value);
            }
        }else if(!repoField.isFieldEmpty(originalValue)){
            if (!updateInUser) dataForReport.put(fieldKey, FieldValue.delete());
            map.put(fieldKey, FieldValue.delete());
        }
    }

    private void fillMap() {
        map = new ArrayMap<>();
        dataForReport = new ArrayMap<>();
        checkValue(FieldConstant.FIELD_NAME_ID, name.getValue(), user.getName(), false);
        checkValue(FieldConstant.FIELD_NAME_2_ID, name2.getValue(), user.getName2(), false);
        checkValue(FieldConstant.FIELD_SURNAME_ID, lastName.getValue(), user.getSurname(), false);
        checkValue(FieldConstant.FIELD_SURNAME_2_ID, lastName2.getValue(), user.getSurname2(), false);
        checkValue(FieldConstant.FIELD_PHONE_ID, phone.getValue(), user.getPhoneNumber(), false);
        checkValue(FieldConstant.FIELD_SEX_ID, sex.getValue(), user.getSex(), true);
        checkValue(FieldConstant.FIELD_BDAY_ID, birthDate.getValue(), user.getBirthDate(), true);
    }

    private void fillMapForRestore(String fieldKey, String newValue, String originalValue){
        if(!repoField.isFieldEmpty(originalValue)){
            if(!newValue.equalsIgnoreCase(originalValue)){
                dataForReport.put(fieldKey, originalValue);
            }else
                dataForReport.remove(fieldKey);
        }else if(!repoField.isFieldEmpty(newValue))
            dataForReport.put(fieldKey, FieldValue.delete()); //viejo
        else
            dataForReport.remove(fieldKey);
    }

    private void restoreInfoForReport(){
        dataForReport.clear();
        fillMapForRestore(FieldConstant.FIELD_NAME_ID, name.getValue(), user.getName());
        fillMapForRestore(FieldConstant.FIELD_NAME_2_ID, name2.getValue(), user.getName2());
        fillMapForRestore(FieldConstant.FIELD_SURNAME_ID, lastName.getValue().toLowerCase(), user.getSurname());
        fillMapForRestore(FieldConstant.FIELD_SURNAME_2_ID, lastName2.getValue(), user.getSurname2());
        fillMapForRestore(FieldConstant.FIELD_PHONE_ID, phone.getValue(), user.getPhoneNumber());
    }

    private void updateUserInfo(final boolean reportExist){
        userRepository.getUserUpdateObservable().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if(Boolean.parseBoolean(((ElementoObservable)o).getElemento().toString())) {
                    opResult.setValue(1);
                }else {
                    opResult.setValue(-1);
                    if(reportExist) {
                        restoreInfoForReport();
                        updateInfoInReports(true);
                    }else
                        setFieldValue();
                }
            }
        });

        userRepository.updateUserInfo(map, curp.getValue());
    }


    private void updateInfoInReports(final boolean restore){
        final ReportUserRepository repository = new ReportUserRepository();
        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        repository.updateUserInfoForReport(dataForReport, curp.getValue());
                    case 2:
                        if(!restore)
                            updateUserInfo(true);
                        else
                            setFieldValue();
                        break;
                    case -1:
                        opResult.setValue(-1);
                        break;
                    case -2:
                        updateUserInfo(false);
                        break;
                }
            }
        });
        if(!restore)
            repository.checkDocumentExistence(curp.getValue());
        else
            repository.updateUserInfoForReport(dataForReport, curp.getValue());
    }

    private void verifyPhone(){
        VerifyPhoneActivity verification = new VerifyPhoneActivity();
        verification.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        fillMap();
                        updateInfoInReports(false);
                        break;
                    case -1:
                    case -2:
                        opResult.setValue(-2);
                        break;
                }
            }
        });
        verification.sendVerificationMethod(phone.getValue());
    }

    private void checkPhoneExistence(){
        userRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        opResult.setValue(-4);
                        break;
                    case -1:
                        opResult.setValue(-1);
                        break;
                    case -2:
                        Log.i(",,,", "Going to verify the phone");
                        verifyPhone();
                        break;
                }
            }
        });
        userRepository.checkDataExistence(FieldConstant.FIELD_PHONE_ID, phone.getValue());
    }

    public void saveChanges(){
        if(!repoField.isFieldEmpty(curp.getValue())) {
            if (checkInfoMatch()) {
                opResult.setValue(-3);
            } else {
                if (!phone.getValue().equals(user.getPhoneNumber())) {
                    Log.i(",,,", "the phone is different");
                    Log.i(",,,FIELD PHONE", phone.getValue());
                    Log.i(",,,USER PHONE", user.getPhoneNumber());
                    checkPhoneExistence();
                }else {
                    fillMap();
                    updateInfoInReports(false);
                }
            }
        }else {
            buttonEnabled.setValue(false);
            opResult.setValue(-5);
        }
    }

    public void checkConnection(){
        checkConnection.setValue(true);
    }
}

