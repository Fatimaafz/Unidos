package com.example.unidos.accountManagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.repository.User;
import com.example.unidos.databinding.FragmentEditInfoBinding;
import com.example.unidos.shared.Dialog;
import com.example.unidos.shared.GeneralCalendar;
import com.example.unidos.shared.KeyBoard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
//changeq

public class EditUserInfo extends Fragment {

    private static int countName = 0;
    private  static final String[] options = new String[]{"Hombre", "Mujer"};
    private boolean getUserInfo;
    private Connection connection;
    int numError, numField;

    private AutoCompleteTextView sex;
    TextInputLayout name, name2, lastName, lastName2, date, tilSex, phone, curp;
    private  ArrayAdapter<String> adapter;

    private AccountManagementViewModel accountManagementViewModel;
    private EditUserInfoViewModel vm;
    private User u;

    public void onCreate(Bundle savedInstanceState) {
        Log.i("@@@@ OnCreate", "Edit info");
        u= new User();
        connection = new Connection(getContext());
        super.onCreate(savedInstanceState);
    }

    public void initFields(View view){
        name = view.findViewById(R.id.TilName);
        name2 = view.findViewById(R.id.TilName2);
        lastName = view.findViewById(R.id.TilLastName);
        lastName2 = view.findViewById(R.id.TilLastName2);
        date = view.findViewById(R.id.TilBirthDate);
        tilSex = view.findViewById(R.id.TilGender);
        phone = view.findViewById(R.id.TilPhone);
        adapter = new ArrayAdapter<>( getContext(), R.layout.dropdown_menu_popup_item, options);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentEditInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_info, container, false);
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(EditUserInfoViewModel.class);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        binding.setVmEditInfo(vm);
        initFields(view);
        sex = view.findViewById(R.id.sexMenu);
        final GeneralCalendar calendar = new GeneralCalendar(getContext());

        TextInputEditText editText = view.findViewById(R.id.TietPhone);
        editText.setTransformationMethod(new KeyBoard());


        vm.onMenuClick.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                sex.setAdapter(adapter);
                sex.showDropDown();
            }
        });

        vm.name.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.validateName(0, s);
            }
        });

        vm.name2.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.validateName(1, s);
            }
        });

        vm.lastName.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.validateName(2, s);
            }
        });

        vm.lastName2.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.validateName(3, s);
            }
        });



        vm.sex.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.checkSex();
            }
        });



        vm.phone.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                vm.validatePhone();
            }
        });

        vm.getConfigCalendar().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean configure) {
                if(configure) {
                    calendar.getDate().addObserver(new java.util.Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            vm.setBirthDate(String.valueOf(((ElementoObservable) o).getElemento()));
                            vm.checkDate();
                        }
                    });
                    calendar.setCalendar(18,100).show();
                }
            }
        });

        vm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        getUserInfo = true;
                        showUserinfo();
                        break;
                    case -1:
                        showToast(Messages.MSG003_1_OP_FAIL);
                        getUserInfo = false;
                        break;
                    case -2:
                        showToast(Messages.MSG005_1_AUTOVERIF_FAILED);
                        getUserInfo = false;
                        break;
                    case -3:
                        showToast(Messages.MSG002_3_NO_INFO_CHANGE);
                        getUserInfo = false;
                        break;
                    case -4:
                        showToast(Messages.MSG002_2_PHONE_DUPLICATED);
                        getUserInfo = false;
                        break;
                    case -5:
                        showToast(Messages.MSG003_1_OP_FAIL);
                        getUserInfo = true;
                        showUserinfo();
                        break;
                }
            }
        });



        vm.getNumField().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer field) {
                numField = field;
                numError = vm.getError();
                switch (field){
                    case 0:
                        name.setErrorEnabled(enableError(numError));
                        name.setError(getErrorMessage());
                        break;
                    case 1:
                        name2.setErrorEnabled(enableError(numError));
                        name2.setError(getErrorMessage());
                        break;
                    case 2:
                        lastName.setErrorEnabled(enableError(numError));
                        lastName.setError(getErrorMessage());
                        break;
                    case 3:
                        lastName2.setErrorEnabled(enableError(numError));
                        lastName2.setError(getErrorMessage());
                        break;
                    case 4:
                        date.setErrorEnabled(enableError(numError));
                        date.setError(getErrorMessage());
                        break;
                    case 5:
                        tilSex.setErrorEnabled(enableError(numError));
                        tilSex.setError(getErrorMessage());
                        break;
                    case 6:
                        phone.setErrorEnabled(enableError(numError));
                        phone.setError(getErrorMessage());
                        break;
                }
            }
        });

        vm.getCheckConnection().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean check) {
                if(check)
                    if(connection.isConnected())
                        vm.saveChanges();
                    else
                        showToast(Messages.MSG004_1_NO_INTERNET);
            }
        });

        return view;
    }

    private void showUserinfo(){
        Log.i("@@@@ ", "IM GOING TO UPDATE");
        Intent i = new Intent(getActivity(), AccountManagement.class);
        i.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private boolean enableError(int error){
        if(error == 1)
            return false;
        else return true;
    }

    private String getErrorMessage(){
        switch (numError){
            case 1:
                return "";
            case 0:
                return Messages.MSG006_1_EMPTY_FIELD;
            case -1:
                if(numField == 6)
                    return Messages.MSG006_2_1_WRONG_PHONE_SYNTAX;
                else
                    return Messages.MSG006_2_2_WRONG_NAME_SYNTAX;
        }
        return "";
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        accountManagementViewModel =  new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);
        accountManagementViewModel.getSharedUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i("@@@@ onCHANGED", "Edit info");
                if(user != null) {
                    Log.i("###", "I received a non null");
                    u= user;
                    vm.setUser(user);
                    vm.setFieldValue();
                }else {
                    Log.i("###", "I received a null");
                    vm.buttonEnabled.setValue(false);
                    //vm.changeFieldState();
                    showToast(Messages.MSG003_1_OP_FAIL);
                }
            }
        });

        accountManagementViewModel.getChangeTab().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean change) {
                Log.i("@@@@", "->><<-I want to change the tab");
                if(change){
                    //if(!(Boolean.parseBoolean(String.valueOf(accountManagementViewModel.getRetrieveInfo()))))
                    if(u != null){
                        vm.setUser(u);
                        vm.setFieldValue();
                    }
                    if(!getUserInfo)
                        accountManagementViewModel.setRetrieveInfo(false);
                }
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
