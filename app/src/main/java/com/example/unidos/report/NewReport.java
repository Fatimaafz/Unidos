package com.example.unidos.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentGenerateReportBinding;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.shared.GeneralCalendar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Observable;

public class NewReport extends Fragment {
    private NewReportViewModel myvm;
    private ReportSharedViewModel sharedViewModel;
    private AutoCompleteTextView autoTown;
    private  static final String[] townOps = new String[]{"Cuauhtémoc", "Gustavo A. Madero"};
    private ReportedPerson reportedPerson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(NewReportViewModel.class);
        Log.i("^^^{", "Oncreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentGenerateReportBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_generate_report, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmNewReport(myvm);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReportSharedViewModel.class);
        final Connection connection = new Connection(getContext());
        final GeneralCalendar calendar = new GeneralCalendar(getContext());

        autoTown = view.findViewById(R.id.townMenu);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, townOps);
        autoTown.setAdapter(adapter);

        sharedViewModel.getPersonReceived().observe(getViewLifecycleOwner(), new Observer<ReportedPerson>() {
            @Override
            public void onChanged(ReportedPerson person) {
                reportedPerson = person;
                myvm.showPersonInfo(person);

            }
        });

        if(connection.isConnected())
            myvm.getUserInfo(new PersistentData().getCURPValue(getContext()));
        else {
            showToast(Messages.ERR_MSSG_01_1);
            showToast(Messages.ERR_MSSG_00);
        }

        myvm.configCalendar.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean configure) {
                Calendar c = Calendar.getInstance();
                if(configure) {
                    calendar.getDate().addObserver(new java.util.Observer() {
                        @Override
                        public void update(Observable o, Object arg) {
                            myvm.checkDate(String.valueOf(((ElementoObservable) o).getElemento()));
                        }
                    });
                    calendar.setCalendar(0, c.get(Calendar.YEAR -1)).show();
                }
            }
        });

        myvm.town.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                myvm.townSelected();
            }
        });


        myvm.addrDetail.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                myvm.checkAddrDetail();
            }
        });

        myvm.getCheckInternetConn().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean check) {
                if(check) {
                    if(reportedPerson!=null){
                        if (reportedPerson.isFound())
                            showToast("La persona ha sido localizada, por lo que no se admiten más reportes.");
                        else {
                            if(connection.isConnected()){
                                myvm.setContext(getContext());
                                myvm.checkIfReportUserExist(false);
                            }else
                                showToast(Messages.ERR_MSSG_00);
                        }
                    }
                }
            }
        });

        myvm.getOpStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        showToast(Messages.OP_SUCCESS_MSSG);
                        sharedViewModel.setRepGenerated(true);
                        break;
                    case -1:
                        showToast(Messages.ERR_MSSG_01);
                        break;
                    case -3:
                        showToast(Messages.ERR_MSSG_01_1);
                        break;
                    case -4:
                        showToast("No se ha encontrado la dirección que proporcionaste");
                        break;
                    case -5:
                        showToast("Ha ocurrido un error al verificar la dirección que proporcionaste");
                        break;
                }
            }
        });

        sharedViewModel.getChangeTab().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean change) {
                if(change){
                    Log.i(",,,", "Going to reset the form");
                    myvm.clearForm();
                }
            }
        });

        return view;
    }

    private void showToast(String msg){
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
