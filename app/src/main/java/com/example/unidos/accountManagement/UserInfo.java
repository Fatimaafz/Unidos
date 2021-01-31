package com.example.unidos.accountManagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.Connection;
import com.example.unidos.Messages;
import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentShowUserInfoBinding;
import com.example.unidos.repository.User;
import com.example.unidos.shared.Dialog;


public class UserInfo extends Fragment {
    private UserInfoViewModel myvm;
    private AccountManagementViewModel accountManagementViewModel;
    private Connection connection;
    private String userCURP;

    public void onCreate(Bundle savedInstanceState) {
        Log.i("@@@@ OnCreate", "Show info");
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UserInfoViewModel.class);
        connection = new Connection(getContext());
        userCURP = new PersistentData().getCURPValue(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("@@@@ onCreateView", "Show info");
        FragmentShowUserInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_user_info, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmUsrInfo(myvm);
        accountManagementViewModel =  new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);

        accountManagementViewModel.setRetrieveInfo(true);

        myvm.setUserCURP(userCURP);

        myvm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        accountManagementViewModel.setSharedUserInfo(myvm.getUser());
                        break;
                    case -1:
                        showToast(Messages.MSG003_1_OP_FAIL);
                        accountManagementViewModel.setSharedUserInfo(myvm.getUser());
                        break;
                    case -2:
                        showToast(Messages.MSG003_1_USER_NOT_FOUND);
                        accountManagementViewModel.setSharedUserInfo(myvm.getUser());
                        break;
                }
            }
        });


        myvm.getIsUserDeleted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDeleted) {
                if(isDeleted)
                    new PersistentData(getContext()).removePreferences();
                else
                    showToast(Messages.ERR_MSSG_01);
            }
        });

        myvm.getLogout().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean logout) {
                if(logout)
                    new PersistentData(getContext()).removePreferences();
            }
        });

        myvm.getIsBtnPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                DialogFragment dialog = new Dialog(false);
                dialog.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        accountManagementViewModel.getRetrieveInfo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean retrieve) {
                Log.i("@@@@ onViewCreated", "Show info");
                if(retrieve){
                    if(connection.isConnected()) {
                        Log.i("@@@@ onViewCreated", "is connected");
                        myvm.getUserInfo();
                    }else {
                        accountManagementViewModel.setSharedUserInfo(null);
                        showToast(Messages.ERR_MSSG_00);
                    }
                }else {
                    myvm.setFieldValue();
                    myvm.showLoadingElement.setValue(false);
                }
            }
        });

        accountManagementViewModel.getIsBtnPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPressed) {
                if(isPressed){
                    myvm.deleteAccount();
                }
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
