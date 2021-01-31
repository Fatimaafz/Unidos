package com.example.unidos.shared;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Messages;
import com.example.unidos.NewAccount;
import com.example.unidos.NewAccountViewModel;
import com.example.unidos.R;
import com.example.unidos.accountManagement.AccountManagement;
import com.example.unidos.accountManagement.AccountManagementViewModel;
import com.example.unidos.accountManagement.UserInfo;
import com.example.unidos.databinding.AlertDialogConfirmationBinding;

public class Dialog extends DialogFragment {
    private AlertDialog dialog;
    private DialogViewModel myvm;
    private AccountManagementViewModel accountManagementViewModel;
    private boolean createAccount;


    public Dialog(boolean createAccount){
        this.createAccount = createAccount;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DialogViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AlertDialogConfirmationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.alert_dialog_confirmation, null, false);
        View view = binding.getRoot();
        binding.setVmDialog(myvm);

        if (createAccount){
            Log.i("%%%%%", "Im creating an account");
            myvm.titleText.setValue("Aviso");
            myvm.setDialogText(Messages.MSG008_2_WARNING);
        }else {
            Log.i("%%%%%", "Im changing the info");
            accountManagementViewModel = new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);
            myvm.titleText.setValue("Confirmación");
            myvm.setDialogText(Messages.MSG008_1_CONFIRMATION);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setView(view);
        dialog = builder.create();

        myvm.getDismiss().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean dismiss) {
                if(!dismiss){
                    Log.i("%%%%%", "Dont want to dismiss");
                    if(createAccount)
                        ((NewAccount)getActivity()).method();
                    else
                        accountManagementViewModel.setIsBtnPressed(true);
                }else {
                    Log.i("%%%%%", "Want to dismiss");
                }
                dismiss();
            }
        });

        return view;
    }
}
