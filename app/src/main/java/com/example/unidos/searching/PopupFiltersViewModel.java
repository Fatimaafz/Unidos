package com.example.unidos.searching;

import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.repository.Field;
import com.example.unidos.repository.FieldConstant;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.google.android.material.slider.RangeSlider;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PopupFiltersViewModel extends ViewModel {
    public MutableLiveData<Boolean> isSearchBtnSelected = new MutableLiveData<>();
    public MutableLiveData<String> text = new MutableLiveData<>();
    public MutableLiveData<List<Float>> array = new MutableLiveData<>();
    private MutableLiveData<Integer> enableError = new MutableLiveData<>();
    public static MutableLiveData<String> selectedOpSex = new MutableLiveData<>();
    public static MutableLiveData<String> selectedOpStatus = new MutableLiveData<>();
    public static MutableLiveData<String> selectedOpPlace = new MutableLiveData<>();
    public static MutableLiveData<Integer> initYear = new MutableLiveData<>();
    public static MutableLiveData<Integer> endYear = new MutableLiveData<>();
    private static MutableLiveData<Boolean> checkConnection = new MutableLiveData<>();
    private MutableLiveData<Integer> opResult = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>();

    private MutableLiveData<Boolean> verifyConn = new MutableLiveData<>();

    private boolean radioBtnSelected;
    private String rbSelectedID = null;
    private ReportedPersonRepository repository;

    public void isInList(boolean isInList){
        repository.setListPeople(isInList);
    }

    public void setUserLocation(LatLng userLocation){
        repository.setUserLocation(userLocation);
    }

    public PopupFiltersViewModel() {
        selectedOpSex = new MutableLiveData<>();
        selectedOpStatus = new MutableLiveData<>();
        selectedOpPlace = new MutableLiveData<>();
        isSearchBtnSelected.setValue(true);
        repository = new ReportedPersonRepository();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        endYear.setValue(year);
        initYear.setValue(year - 3);
        showProgressBar.setValue(false);
    }

    public MutableLiveData<Integer> getEnableError() {
        return enableError;
    }

    private void setEnableError(int enable){
        enableError.setValue(enable);
    }

    private void setIsSearchBtnSelected(boolean isSearchBtnSelected) {
        this.isSearchBtnSelected.setValue(isSearchBtnSelected);
    }

    public MutableLiveData<Boolean> getCheckConnection() {
        return checkConnection;
    }

    private void setCheckConnection(boolean check) {
        checkConnection.setValue(check);
    }

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    private void setOpResult(int resultID) {
        opResult.setValue(resultID);
    }

    public boolean isRadioButtonEmpty(){
        if(radioBtnSelected)
            return false;
        else return true;
    }

    public void checkSearchAndRadioBtn(){
        //isSearchBtnSelected.setValue(false);
        doOnEditTextState(isRadioButtonEmpty());
    }

    private void doOnEditTextState(boolean isRbEmpty){
        boolean isEditTextEmpty = isFieldEmpty(text.getValue());
        if(isEditTextEmpty && isRbEmpty || !isEditTextEmpty && !isRbEmpty){
            setEnableError(1);
            setIsSearchBtnSelected(true);
        }else{
            setIsSearchBtnSelected(false);
            if(!isEditTextEmpty && isRbEmpty)
                setEnableError(-1);
            else if(isEditTextEmpty && !isRbEmpty)
                setEnableError(-2);
        }
    }

    public boolean isFieldEmpty(String value){
        if(value == null){
            return true;
        }else if(value.isEmpty())
            return true;
        else
            return false;
    }

    public void curpSelected(){
        Log.i("-->><<--", "CURP selected");
        rbSelectedID = FieldConstant.FIELD_CURP_ID;
        radioBtnSelected = true;
        doOnEditTextState(!radioBtnSelected);
    }

    public void nameSelected(){
        rbSelectedID = FieldConstant.FIELD_NAME_ID;
        Log.i("-->><<--", "Name selected");
        radioBtnSelected = true;
        doOnEditTextState(!radioBtnSelected);
    }

    public void lastNameSelected(){
        rbSelectedID = FieldConstant.FIELD_SURNAME_ID;
        Log.i("-->><<--", "Last Name selected");
        radioBtnSelected = true;
        doOnEditTextState(!radioBtnSelected);
    }

    @BindingAdapter("rangeVal")
    public static void setVal(RangeSlider rangeSlider, List<Float> values){
        rangeSlider.setValues(values.get(0), values.get(1));
    }

    @InverseBindingAdapter(attribute = "rangeVal")
    public static List<Float> setVal(RangeSlider rangeSlider){
        return rangeSlider.getValues();
    }

    @BindingAdapter("app:rangeValAttrChanged")
    public static void setListeners(RangeSlider rangeSlider, final InverseBindingListener attrChange){
        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                attrChange.onChange();
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                attrChange.onChange();
            }
        });
    }

    @BindingAdapter("onItemSelected")
    public static void setOptionText(AutoCompleteTextView autoCompleteTextView, CharSequence charSequence){
        //autoCompleteTextView.setText(charSequence);
    }

    @InverseBindingAdapter(attribute = "onItemSelected")
    public static String getOptionText(AutoCompleteTextView autoCompleteTextView){
        return autoCompleteTextView.getText().toString();
    }

    @BindingAdapter("app:onItemSelectedAttrChanged")
    public static void setAutoCompleteListener(final AutoCompleteTextView autoCompleteTextView, final InverseBindingListener attrChange){
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                Log.i("-->><<--", selection);
                Log.i("-->><<-- hint", autoCompleteTextView.getHint().toString());
                attrChange.onChange();
            }
        });
    }

    private String changeTextFormat(){
        if(rbSelectedID.equals(FieldConstant.FIELD_CURP_ID))
            return text.getValue().toUpperCase().trim();
        else
            return text.getValue().toLowerCase().trim();
    }

    private List<Field> getPopulatedList(){
        //Log.i("-->><<-- ID DEL BT", rbSelectedID);
        //Log.i("-->><<-- VALOR", changeTextFormat());
        List<Field> fieldList = new ArrayList<>();
        Field field = new Field();

        fieldList.add(new Field(array.getValue().get(0).intValue() , array.getValue().get(1).intValue()));

        if (!isFieldEmpty(text.getValue())){
            fieldList.add(new Field(rbSelectedID, changeTextFormat()));
        }

        if(!isFieldEmpty(selectedOpStatus.getValue())){
            field = new Field(FieldConstant.FIELD_STATUS_ID, field.statusToBool(selectedOpStatus.getValue()));
            fieldList.add(field);
        }

        if(!isFieldEmpty(selectedOpSex.getValue())) {
            field = new Field(FieldConstant.FIELD_SEX_ID, field.sexToInt(selectedOpSex.getValue()));
            fieldList.add(field);
        }

        if(!isFieldEmpty(selectedOpPlace.getValue())){
            field = new Field(FieldConstant.FIELD_PLACE_ID, field.placeToInt(selectedOpPlace.getValue()));
            fieldList.add(field);
        }
        return fieldList;
    }

    public List<ReportedPerson> getCoincidences(){
        Log.i("######", "List "+ repository.getPersonList());
        return repository.getPersonList();
    }

    public void applyFilters(){
        showProgressBar.setValue(true);
        Log.i("######", "APPLY FILTER");
        List<Field> fieldList;
        fieldList = getPopulatedList();

        //Log.i("-->><<-- CONV CURP", fieldList.get(1).getValue().toString());

        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        setOpResult(1);
                        showProgressBar.setValue(false);
                        break;
                    case -1:
                        setOpResult(-1);
                        break;
                    case -2:
                        Log.i("^^^{ vm", "LIST is empty");
                        setOpResult(-2);
                        break;
                }
                showProgressBar.setValue(false);
            }
        });

        switch (fieldList.size()){
            case 1:
                repository.filterByDate(fieldList);
                break;
            case 2:
                repository.applyOneFilter(fieldList);
                break;
            case 3:
                repository.applyTwoFilters(fieldList);
                break;
            case 4:
                repository.applyThreeFilters(fieldList);
                break;
            case 5:
                repository.applyFourFilters(fieldList);
                break;
        }
    }

    public void prepareForFilter() {
        Log.i("######", "I click the button");
        //checkConnection.setValue(true);
        verifyConn.setValue(true);
    }

    public MutableLiveData<Boolean> getVerifyConn() {
        return verifyConn;
    }
}
