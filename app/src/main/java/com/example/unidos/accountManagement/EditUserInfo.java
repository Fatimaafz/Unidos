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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.User;
import com.example.unidos.databinding.FragmentEditInfoBinding;
import com.example.unidos.shared.KeyBoard;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;


import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
//changeq

public class EditUserInfo extends Fragment {

    private static int countName = 0;
    private  static final String[] options = new String[]{"Hombre", "Mujer"};
    private String selection = "";
    private boolean getUserInfo;
    Messages messages = new Messages();

    AutoCompleteTextView sex;
    TextInputLayout name, name2, lastName, lastName2, date, tilSex, phone, curp;

    private AccountManagementViewModel accountManagementViewModel;
    private EditUserInfoViewModel vm;

    /* --- > @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        FragmentEditInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_info, container, false);
        accountManagementViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AccountManagementViewModel.class);
        view = binding.getRoot();
        binding.setVmEditUserInfo(accountManagementViewModel);

        return view;
    } */


    public void onCreate(Bundle savedInstanceState) {
        Log.i("@@@@ OnCreate", "Edit info");
        super.onCreate(savedInstanceState);
    }


    public void onResume(){
        super.onResume();
        Log.i("@@@@-----", "onResume----");
    }

    public void initFields(View view){
        name = view.findViewById(R.id.TilName);
        name2 = view.findViewById(R.id.TilName2);
        lastName = view.findViewById(R.id.TilLastName);
        lastName2 = view.findViewById(R.id.TilLastName2);
        date = view.findViewById(R.id.TilBirthDate);
        tilSex = view.findViewById(R.id.TilGender);
        phone = view.findViewById(R.id.TilPhone);
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
        Log.i("@@@@ -----", "------");

        String[] COUNTRIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>( getContext(), R.layout.dropdown_menu_popup_item, COUNTRIES);

        AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.sexMenu);
        editTextFilledExposedDropdown.setAdapter(adapter);

        TextInputEditText editText = view.findViewById(R.id.TietPhone);
        editText.setTransformationMethod(new KeyBoard());

        vm.setContext(getContext());


        vm.name.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                countName++;
                Log.i("@@@@ Count ", String.valueOf(countName));
                Log.i("@@@@ Field: ", "0");
                vm.setNumberOfField(0);
                vm.validateName(s);
            }
        });

        vm.name2.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("@@@@ Field: ", "1");
                vm.setNumberOfField(1);
                vm.validateName(s);
            }
        });

        vm.lastName.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("@@@@ Field: ", "2");
                vm.setNumberOfField(2);
                vm.validateName(s);
            }
        });

        vm.lastName2.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("@@@@ Field: ", "3");
                vm.setNumberOfField(3);
                vm.validateName(s);
            }
        });

        vm.isFieldDateSelected.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                DatePickerDialog datePickerDialog = setCalendar(vm);
                /** show the calendar */
                datePickerDialog.show();
            }
        });

        vm.isFieldSexSelected.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                setDropDownMenu(vm);
                sex.showDropDown();
            }
        });

        vm.phone.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("@@@@ Field: ", "6");
                vm.validatePhone();
            }
        });

        vm.fieldError.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case 0 :
                        name.setErrorEnabled(true);
                        name.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 1 :
                        name2.setErrorEnabled(true);
                        name2.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 2 :
                        lastName.setErrorEnabled(true);
                        lastName.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 3 :
                        lastName2.setErrorEnabled(true);
                        lastName2.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 4:
                        date.setErrorEnabled(true);
                        date.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 5:
                        tilSex.setErrorEnabled(true);
                        tilSex.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                    case 6:
                        phone.setErrorEnabled(true);
                        phone.setError(messages.findMessage(vm.getErrorMessage()));
                        break;
                }
            }
        });

        vm.validField.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer) {
                    case 0:
                        name.setErrorEnabled(false);
                        break;
                    case 1:
                        name2.setErrorEnabled(false);
                        break;
                    case 2:
                        lastName.setErrorEnabled(false);
                        break;
                    case 3:
                        lastName2.setErrorEnabled(false);
                        break;
                    case 4:
                        date.setErrorEnabled(false);
                        break;
                    case 5:
                        tilSex.setErrorEnabled(false);
                        break;
                    case 6:
                        phone.setErrorEnabled(false);
                        break;
                }
            }
        });

        vm.opResult.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Messages msg = new Messages();
                if(s.equals("update")) {
                    Toast.makeText(getContext(), messages.findMessage(s), Toast.LENGTH_SHORT).show();
                    Log.i("@@@@ ", "IM GOING TO UPDATE");
                    Intent i = new Intent(getActivity(), AccountManagement.class);
                    i.addFlags(FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    getUserInfo = true;
                }else {
                    Log.i("@@@@ ", "I detected a change");
                    Toast.makeText(getActivity(), msg.findMessage(s), Toast.LENGTH_SHORT).show();
                    getUserInfo = false;
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.i("@@@@-----", "onActivityCreated----");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("@@@@ onViewCreated", "Edit info");
        accountManagementViewModel =  new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);

        accountManagementViewModel.getSharedUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.i("@@@@ onCHANGED", "Edit info");
                if(user != null) {
                    vm.setUser(user);
                    vm.setFieldValue();
                }else
                    Toast.makeText(getContext(), messages.findMessage("fail"), Toast.LENGTH_SHORT);
            }
        });

        accountManagementViewModel.getChangeTab().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean change) {
                Log.i("@@@@", "->><<-I want to change the tab");
                if(change){
                    //if(!(Boolean.parseBoolean(String.valueOf(accountManagementViewModel.getRetrieveInfo()))))
                    if(!getUserInfo)
                        accountManagementViewModel.setRetrieveInfo(false);
                }
            }
        });

    }


    public DatePickerDialog setCalendar(final EditUserInfoViewModel vm){
        final Calendar c = Calendar.getInstance();
        /** get the day, month and year **/
        final int mYear = c.get(Calendar.YEAR), mMonth = c.get(Calendar.MONTH), mDay = c.get(Calendar.DAY_OF_MONTH);
        /** The listener indicate the user has finished selecting a date. **/
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                /** set a date right format to a variable. **/
                String selectedDate = "";
                if(dayOfMonth<10 && month<10) {
                    selectedDate = "0"+dayOfMonth + "/0" + (month+1) + "/" + year;
                }else if(dayOfMonth<10)
                    selectedDate = "0"+dayOfMonth + "/" + (month+1) + "/" + year;
                else if(month<10)
                    selectedDate = dayOfMonth + "/0" + (month+1) + "/" + year;
                else
                    selectedDate = ""+dayOfMonth + "/" + (month+1) + "/" + year;
                /** call to the method in order to pass the date selected on every click **/
                vm.checkDate(selectedDate);
            }
        }, mYear, mMonth, mDay);

        /** Configure the date to display, the system wont allow register minors **/
        Date today= new Date();
        c.setTime(today);
        c.add(Calendar.YEAR, -18);
        long maxDate = c.getTime().getTime();
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        c.setTime(today);
        c.add(Calendar.YEAR, -100);
        long minDate= c.getTime().getTime();
        datePickerDialog.getDatePicker().setMinDate(minDate);
        return datePickerDialog;
    }

    public void setDropDownMenu(final EditUserInfoViewModel vm){
        System.out.println("DEBO MOSTRAR EL MENÚ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, options);
        sex.setAdapter(adapter);
        sex.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            /** To know which option was selected. **/
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
                selection = (String) arg0.getItemAtPosition(arg2);
                Log.i("SELECTED TEXT WAS---->", selection);
                /** To send the value to NewAccountViewModel **/
                vm.checkSex(selection);
            }
        });
    }

    @Override
    public void onPause() {
        Log.i("@@@@ ----> onPause", "Edit info");
        super.onPause();
    }

    public void onStop() {
        Log.i("@@@@ onStop", "Show info");
        super.onStop();

    }
}
