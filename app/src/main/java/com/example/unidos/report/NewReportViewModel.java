package com.example.unidos.report;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.renderscript.Script;
import android.util.ArrayMap;
import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.repository.FieldConstant;
import com.example.unidos.repository.ReportRepository;
import com.example.unidos.repository.ReportUserRepository;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.example.unidos.repository.User;
import com.example.unidos.repository.UserRepository;
import com.example.unidos.repository.Field;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class NewReportViewModel extends ViewModel {
    public MutableLiveData<String> userCURP = new MutableLiveData<>();
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> userPhone = new MutableLiveData<>();
    public MutableLiveData<String> personCURP = new MutableLiveData<>();
    public MutableLiveData<String> personName = new MutableLiveData<>();
    public MutableLiveData<String> age = new MutableLiveData<>();
    public MutableLiveData<String> elabDate = new MutableLiveData<>();
    public MutableLiveData<String> seenDate = new MutableLiveData<>();
    public MutableLiveData<String> town = new MutableLiveData<>();
    public MutableLiveData<String> addrDetail = new MutableLiveData<>();
    public MutableLiveData<String> clothes = new MutableLiveData<>();
    public MutableLiveData<String> details = new MutableLiveData<>();
    public MutableLiveData<Boolean> configCalendar = new MutableLiveData<>();
    public static MutableLiveData<Boolean> isGenerateBtnSelected = new MutableLiveData<>();
    public static MutableLiveData<Integer> opStatus;
    public MutableLiveData<Boolean> enableDateError = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableTownError = new MutableLiveData<>();
    public MutableLiveData<Boolean> enableAddrError = new MutableLiveData<>();
    public MutableLiveData<String> addrError = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkInternetConn = new MutableLiveData<>();
    private Date registeredSeenDate;
    private ReportedPerson reportedPerson;
    private Context context;
    private GeoPoint recLocation;
    private User user;
    Map<String, Object> mapPersonInfo;

    private static List<Boolean> necessaryInfo;
    private Field field;

    public void setContext(Context context) {
        this.context = context;
    }

    public NewReportViewModel(){
        opStatus = new MutableLiveData<>();
        necessaryInfo = new ArrayList<>();
        field = new Field();
        elabDate.setValue(field.dateToSimpleDate(Calendar.getInstance().getTime()));
        enableTownError.setValue(true);
        enableAddrError.setValue(true);
        enableDateError.setValue(true);

        necessaryInfo.add(false);
        necessaryInfo.add(false);
        necessaryInfo.add(false);
        necessaryInfo.add(false);
        necessaryInfo.add(false);
    }

    public static MutableLiveData<Integer> getOpStatus() {
        return opStatus;
    }

    public MutableLiveData<Boolean> getCheckInternetConn() {
        return checkInternetConn;
    }

    private void checkNecessaryInfo(){
        Log.i(",,, Per map", "Check necessary info");

        int fillField = 0;
        int size = necessaryInfo.size();
        for (boolean state : necessaryInfo){
            Log.i(",,, state", String.valueOf(state));
            if(state)
                fillField++;
        }
        if(fillField == size) {
            isGenerateBtnSelected.setValue(true);
        }else {
            isGenerateBtnSelected.setValue(false);
        }
    }

    public void showPersonInfo(ReportedPerson person){
        Log.i(",$,$ ", "show person info");
        reportedPerson = person;
        necessaryInfo.set(1, true);
        personCURP.setValue(person.getCurp());
        personName.setValue(person.getFullName());
        age.setValue(String.valueOf(person.getAge()));
        registeredSeenDate = person.getRecSeenDate();
    }

    private void showUserInfo(User user){
        userCURP.setValue(user.getCURP());
        userName.setValue(user.getFullName());
        userPhone.setValue(user.getPhoneNumber());
    }

    public void getUserInfo(String CURP){
        final UserRepository userRepository = new UserRepository();
        userRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                int result = (int) ((ElementoObservable) o).getElemento();
                if(result == 1) {
                    user = userRepository.getUser();
                    showUserInfo(user);
                    necessaryInfo.set(0, true);
                }else {
                    showUserInfo(new User());
                    necessaryInfo.set(0, false);
                    opStatus.setValue(-3);
                }
                checkNecessaryInfo();
            }
        });
        userRepository.retrieveUserData(CURP);
    }

    @BindingAdapter("app:errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(Messages.FIELD_ERR_01);
    }

    public void showCalendar(){
        if (field.isFieldEmpty(seenDate.getValue()))
            enableDateError.setValue(true);
        else
            enableDateError.setValue(false);
        configCalendar.setValue(true);
    }


    public void checkDate(String date){
        seenDate.setValue(date);
        enableDateError.setValue(false);
        necessaryInfo.set(2, true);
        checkNecessaryInfo();
    }

    public void checkAddrDetail(){
        if(field.isFieldEmpty(addrDetail.getValue())) {
            enableAddrError.setValue(true);
            addrError.setValue("");
            necessaryInfo.set(4, false);
        }else{
            enableAddrError.setValue(false);
            necessaryInfo.set(4, true);
        }
        checkNecessaryInfo();
    }

    public void townSelected(){
        enableTownError.setValue(false);
        necessaryInfo.set(3, true);
        checkNecessaryInfo();
    }

    public void configToGenerate(){
        checkInternetConn.setValue(true);
    }

    private int isSameLocation(){
        try {
            List<Address> addressList = new Geocoder(context).getFromLocationName("México, Ciudad de México, " + town.getValue()
                    + ", " + addrDetail.getValue(), 1);
            Log.i("###### ADDR", "México, Ciudad de México, " + town.getValue()
                    + ", " + addrDetail.getValue());
            if(addressList.isEmpty() || addressList == null) {
                Log.i("###### ", "LIST IS EMPTY");
                opStatus.setValue(-4);
                return -1;
            }else if(reportedPerson.getRecLocation().getLatitude() == addressList.get(0).getLatitude()
            && reportedPerson.getRecLocation().getLongitude() == addressList.get(0).getLongitude()) {
                Log.i("###### ", "ADDR IS THE SAME");
                return 0;
            }else{
                recLocation = new GeoPoint(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                Log.i("###### LAT: ", String.valueOf(addressList.get(0).getLatitude()));
                Log.i("###### LON: ", String.valueOf(addressList.get(0).getLongitude()));
                Log.i("###### ", "ADDR DIFF");
                return 1;
            }

        } catch (IOException e) {
            Log.i("###### EXCEPTION", e.getMessage());
            e.printStackTrace();
            opStatus.setValue(-5);
            return -1;
        }
    }

    private int fillPersonMap(){
        mapPersonInfo = new ArrayMap<>();
        if(! (registeredSeenDate.compareTo(field.getDateFromString(seenDate.getValue())) > 0)) {
            mapPersonInfo.put(FieldConstant.FIELD_DATE_ID, field.getDateFromString(seenDate.getValue()));
            if (field.placeToInt(town.getValue()) != reportedPerson.getMissingPlace())
                mapPersonInfo.put(FieldConstant.FIELD_PLACE_ID, field.placeToInt(town.getValue()));
            switch (isSameLocation()){
                case 1:
                    Log.i(",,, Per map", mapPersonInfo.toString());
                    mapPersonInfo.put(FieldConstant.FIELD_RECENT_LOC_ID, recLocation);
                    return 1;
                case -1:
                    Log.i(",,, Per map", "ADDR Does not exist");
                    return -1;
                case 0:
                    return 1;
                default:
                    return -1;
            }
        }else{
            Log.i(",,, Per map", "Is null because date is older than registered");
            return -2;
        }
    }

    private Map<String, Object> fillPersonMapForRestore() {
        Map<String, Object> attrMap = new ArrayMap<>();
        attrMap.put(FieldConstant.FIELD_DATE_ID, reportedPerson.getRecSeenDate());
        attrMap.put(FieldConstant.FIELD_PLACE_ID, reportedPerson.getMissingPlace());
        attrMap.put(FieldConstant.FIELD_RECENT_LOC_ID, reportedPerson.getRecLocation());
        Log.i(",,, Per map for restore", attrMap.toString());
        return attrMap;
    }

    private void updateInPerson(final boolean reportUserExist, final boolean restore){
        Log.i("up ", "Im in update in peroson");
        ReportedPersonRepository reportedPersonRepository = new ReportedPersonRepository();
        reportedPersonRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if ((int) ((ElementoObservable) o).getElemento() == 1) {
                    Log.i(",,,up ", "I update USR info");
                    if (!restore) {
                        Log.i(",,,up ", "lets generate report after update in person");
                        generateReport(reportUserExist, true);
                    }else if (!reportUserExist) {
                        Log.i(",,,up ", "Lets restore reportInfo values");
                        checkIfReportUserExist(true);
                    }
                }else {
                    Log.i(",,,up ", "Fail updating person info");
                    if(!reportUserExist) {
                        Log.i(",,,up ", "Lets delete reportUser cause fail while updating person info");
                        checkIfReportUserExist(true);
                    }
                    opStatus.setValue(-1);
                }
            }
        });

        if(!restore){
            Log.i(",,,up ", "Dont want to restore");
            switch (fillPersonMap()){
                case 1:
                case 0:
                    Log.i(",,,up ", "map is filled, lest update in person");
                    reportedPersonRepository.updateRecentSeenDate(mapPersonInfo, personCURP.getValue());
                    break;
                case -2:
                    Log.i(",,,", "lets generate report cause date is older");
                    generateReport(reportUserExist, false);
                    break;
            }
        }else {
            Log.i(",,,up ", "I want to restore");
            reportedPersonRepository.updateRecentSeenDate(fillPersonMapForRestore(), personCURP.getValue());
        }
    }

    private Map<String, Object> fillMapForReportUser(){
        Map<String, Object> userInfoForReport = new ArrayMap<>();
        userInfoForReport.put(FieldConstant.FIELD_NAME_ID, user.getName());
        userInfoForReport.put(FieldConstant.FIELD_SURNAME_ID, user.getSurname());
        userInfoForReport.put(FieldConstant.FIELD_PHONE_ID, userPhone.getValue());
        if(!field.isFieldEmpty(user.getName2()))
            userInfoForReport.put(FieldConstant.FIELD_NAME_2_ID, user.getName2());

        if(!field.isFieldEmpty(user.getSurname2()))
            userInfoForReport.put(FieldConstant.FIELD_SURNAME_2_ID, user.getSurname2());

        Log.i(",,, map RepUser", userInfoForReport.toString());
        return userInfoForReport;
    }

    public void checkIfReportUserExist(final boolean restore){
        final ReportUserRepository repository = new ReportUserRepository();
        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        Log.i(",,,nr ", "reportUser exist");
                        Log.i(",,,nr ", "lets update in person after update reportUser");
                        updateInPerson(true, false);
                        break;
                    case 2:
                        Log.i(",,,nr ", "I create reportUser");
                        if(!restore) {
                            Log.i(",,,nr ", "lets update in person after create reportUser");
                            updateInPerson(false, false);
                        }
                        break;
                    case -1:
                        Log.i(",,,nr ", "Fail");
                        opStatus.setValue(-1);
                        break;
                    case -2:
                        Log.i(",,,nr ", "reportUser NO exist, lets creat it");
                        repository.createReportUser(fillMapForReportUser(), userCURP.getValue());
                        break;
                }
            }
        });
        if(!restore) {
            Log.i(",,,nr ", "I dont want to restore");
            repository.checkDocumentExistence(userCURP.getValue());
        }else {
            Log.i(",,,nr ", "I want to restore");
            repository.deleteReportUser(userCURP.getValue());
        }

    }

    private void generateReport(final boolean reportUserExist, final boolean reportInfoUpdated){
        Log.i(",,,gr", "im in generate report");
        ReportRepository reportRepository = new ReportRepository();
        reportRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if((int) ((ElementoObservable) o).getElemento() == 1) {
                    Log.i(",,, gr", "I create successfully the report");
                    opStatus.setValue(1);
                }else {
                    Log.i(",,, gr", "Fail creating report, lets restore info in person");
                    if(reportInfoUpdated) {
                        Log.i(",,, gr", "Debemos restaurar la info en persona");
                        updateInPerson(reportUserExist, true);
                    }else{
                        Log.i(",,, gr", "NO Debemos restaurar la info en persona");
                        if(reportUserExist) {
                            Log.i(",,, gr", "Debemos restaurar la info en persona");
                            checkIfReportUserExist(true);
                        }
                    }
                    opStatus.setValue(-1);
                }
            }
        });
        reportRepository.generateReport(getReportMap());
    }

    private Map<String, Object> getReportMap(){
        Map<String, Object> reportMap = new HashMap<>();
        reportMap.put(FieldConstant.FIELD_USER_ID, userCURP.getValue());
        reportMap.put(FieldConstant.FIELD_PERSON_ID, personCURP.getValue());
        reportMap.put(FieldConstant.FIELD_TYPE_ID, 1);
        reportMap.put(FieldConstant.FIELD_ELAB_ID, field.getDateFromString(elabDate.getValue()));
        reportMap.put(FieldConstant.FIELD_SEEN_DATE_ID, field.getDateFromString(seenDate.getValue()));
        reportMap.put(FieldConstant.FIELD_TOWN_ID, town.getValue());
        reportMap.put(FieldConstant.FIELD_ADDR_DETAIL_ID, addrDetail.getValue());

        if(!field.isFieldEmpty(clothes.getValue()))
            reportMap.put(FieldConstant.FIELD_CLOTHE_ID, clothes.getValue());

        if(!field.isFieldEmpty(details.getValue()))
            reportMap.put(FieldConstant.FIELD_DETAIL_ID, details.getValue());
        Log.i(",,, report map", reportMap.toString());
        return reportMap;
    }

    public void clearForm(){
        seenDate.setValue("");
        town.setValue("");
        addrDetail.setValue("");
        clothes.setValue("");
        details.setValue("");
    }
}
