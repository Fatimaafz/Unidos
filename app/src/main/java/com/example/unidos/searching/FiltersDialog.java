package com.example.unidos.searching;

import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Connection;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.PopUpFiltersBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class FiltersDialog extends androidx.fragment.app.DialogFragment {
    private AlertDialog dialogFilters;
    private PopupFiltersViewModel popupFiltersViewModel;
    private SearchViewModel searchViewModel;
    private TextInputLayout searchTil;
    private AutoCompleteTextView sex, town, status;
    private Connection connection;
    private  static final String[] sexOptions = new String[]{"Hombre", "Mujer"};
    private  static final String[] townOptions = new String[]{"Gustavo A. Madero", "Cuauhtémoc"};
    private  static final String[] statusOptions = new String[]{"Localizado", "No localizado"};
    private boolean wantToList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("######", "on create");
        popupFiltersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PopupFiltersViewModel.class);
        connection = new Connection(getContext());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("-->><<--", "Im creating the view");
        searchViewModel =  new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        PopUpFiltersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.pop_up_filters, null, false);
        binding.setLifecycleOwner(this);
        final View view = binding.getRoot();
        binding.setVmPopupFilters(popupFiltersViewModel);
        builder.setView(view);
        dialogFilters = builder.create();
        //dialogFilters.show();

        searchTil = view.findViewById(R.id.TilCurp);
        sex = view.findViewById(R.id.sexMenu);
        status = view.findViewById(R.id.statusMenu);
        town = view.findViewById(R.id.townMenu);

        ArrayAdapter<String> adapter;

        List<Float> arr = new ArrayList<>();
        arr.add((float) 2018);
        arr.add((float) 2020);

        popupFiltersViewModel.array.setValue(arr);

        searchViewModel.getIsInList().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isInList) {
                Log.i("###### user is", String.valueOf(isInList));
                popupFiltersViewModel.isInList(isInList);
                wantToList = isInList;
            }
        });

        popupFiltersViewModel.text.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                popupFiltersViewModel.checkSearchAndRadioBtn();
            }
        });

        popupFiltersViewModel.array.observe(getViewLifecycleOwner(), new Observer<List<Float>>() {
            @Override
            public void onChanged(List<Float> floats) {
                Log.i("-->><<-- 0", String.valueOf(floats.get(0)));
                Log.i("-->><<-- 1", String.valueOf(floats.get(1)));
            }
        });

        adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, sexOptions);
        sex.setAdapter(adapter);
        /*sex.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                String selection = (String) arg0.getItemAtPosition(arg2);
                Log.i("-->><<-- Selected sex ", selection);
                //vm.checkSex(selection);
            }
        });*/

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, townOptions);
        town.setAdapter(adapter2);
        adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, statusOptions);
        status.setAdapter(adapter);

        popupFiltersViewModel.getEnableError().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer error) {
                switch (error){
                    case 1:
                        searchTil.setErrorEnabled(false);
                        break;
                    case -1:
                        searchTil.setErrorEnabled(true);
                        searchTil.setError("Debes seleccionar CURP, nombre o apellido");
                        break;
                    case -2:
                        searchTil.setErrorEnabled(true);
                        searchTil.setError("Debes ingresar: CURP, nombre o apellido");
                }
            }
        });

        // ¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡¡ REMOVE !!!!!!!!!!!!!!!!!!!
        /*popupFiltersViewModel.getCheckConnection().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("#######", "Lets configure");
                if(connection.isConnected()) {
                    if (wantToList){
                        Log.i("#######", "I think im in the list");
                        //popupFiltersViewModel.applyFilters();
                    }else {
                        Log.i("######", "I want to get the location");
                        searchViewModel.setWantToGetLocation(true);
                    }
                }else
                    showToast(Messages.ERR_MSSG_00);
            }
        });*/

        popupFiltersViewModel.getVerifyConn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    Log.i("#######", "I MUST CONFIGURE");
                    if(connection.isConnected()) {
                        if (wantToList){
                            Log.i("#######", "I think im in the list");
                            popupFiltersViewModel.applyFilters();
                        }else {
                            Log.i("######", "I want to get the location");
                            searchViewModel.setWantToGetLocation(true);
                        }
                    }else
                        showToast(Messages.ERR_MSSG_00);
                }else
                    Log.i("#######", "I MUSNT im in the list");
            }
        });

        searchViewModel.getUserLocation().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng location) {
                if(location != null) {
                    Log.i("######", "BLABLABLAB");
                    popupFiltersViewModel.setUserLocation(location);
                    searchViewModel.setUserLocation(null);
                    popupFiltersViewModel.applyFilters();
                }
            }
        });

        searchViewModel.getTest().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.i("######", "Aply filters BLABLAB");
                }else
                    Log.i("######", "Show location error");
            }
        });

        popupFiltersViewModel.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer opResult) {
                switch (opResult){
                    case 1:
                        Log.i("######", "Im going to send the list");
                        searchViewModel.setDismissDialog(true);
                        if(wantToList)
                            searchViewModel.setCoincidences(popupFiltersViewModel.getCoincidences());
                        else
                            searchViewModel.setCoincidencesForMap(popupFiltersViewModel.getCoincidences());
                        showToast(Messages.MSG001_1_OP_SUCCESS);
                        break;
                    case -1:
                        showToast(Messages.ERR_MSSG_01);
                    case -2:
                        showToast(Messages.ERR_MSSG_02);
                        Log.i("^^^{ class", "LIST is empty");
                        searchViewModel.setCoincidencesForMap(null);
                        break;
                }
            }
        });


        return view;
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}

/*
private AlertDialog.Builder builder;
    private AlertDialog dialogFilters;
builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        //View filtersPopup = getLayoutInflater().inflate(R.layout.pop_up_filters, null);
        final PopupFiltersViewModel popupFiltersViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(PopupFiltersViewModel.class);
        PopUpFiltersBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.pop_up_filters, null, false);
        final View view = binding.getRoot();
        binding.setVmPopupFilters(popupFiltersViewModel);
        builder.setView(view);
        dialogFilters = builder.create();
        dialogFilters.show();
* */
