package com.example.unidos.accountManagement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
//changeq
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentShowUserInfoBinding;


public class UserInfo extends Fragment {
    private Messages msg = new Messages();
    private UserInfoViewModel myvm;
    private AccountManagementViewModel accountManagementViewModel;
    private LinearLayout linearLayout;
    private ConstraintLayout constraintLayout;


    private void showMessage(String m){
        Toast.makeText(getContext(), msg.findMessage(m), Toast.LENGTH_SHORT).show();
    }

    private void hideLoadingElement(){
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.i("@@@@ OnCreate", "Show info");
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(UserInfoViewModel.class);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("@@@@ onAttach", "Show info");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@@@ onResume", "Show info");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("@@@@ onCreateView", "Show info");
        FragmentShowUserInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_user_info, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmUsrInfo(myvm);
        //View view = inflater.inflate(R.layout.fragment_show_user_info, container, false);
        accountManagementViewModel =  new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);

        accountManagementViewModel.setRetrieveInfo(true);

        myvm.setContext(getContext());
        myvm.setView(view);
        myvm.setAccountManagementViewModel(accountManagementViewModel);

        linearLayout = view.findViewById(R.id.linearLayoutShowUserInfo);
        constraintLayout = view.findViewById(R.id.constraintLayoutShowUserInfo);

        /*myvm.getUserInfoResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), msg.findMessage(s), Toast.LENGTH_SHORT).show();
            }
        });*/

        /*myvm.infoExist.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    linearLayout.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                }else{
                    linearLayout.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);
                }
            }
        });
        myvm.getUserInfo();*/ //<-----------

        /*textInputEditText = view.findViewById(R.id.et);
        Button button = view.findViewById(R.id.mybt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("***", textInputEditText.getText().toString());
                accountManagementViewModel.setSharedText(textInputEditText.getText().toString());
            }
        });*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.i("@@@@ onActCreated", "Show info");
        //accountManagementViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AccountManagementViewModel.class);

        /*accountManagementViewModel.getSharedText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("***", s);
                textInputEditText.setText(s);
            }
        });*/

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.i("@@@@ onStart", "Show info");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("@@@@ onViewCreated", "Show info");

        linearLayout.setVisibility(View.VISIBLE);
        constraintLayout.setVisibility(View.GONE);


        accountManagementViewModel.getRetrieveInfo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean retrieve) {
                if(retrieve){
                    myvm.getUserInfoResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (s.equals("success")){
                                Log.i("@@@@ @", "LETS UPDATE");
                                hideLoadingElement();
                            }else {
                                hideLoadingElement();
                                showMessage(s);
                            }
                        }
                    });
                    myvm.getUserInfo();
                }else {
                    //Toast.makeText(getActivity(), "No se detectaron modificaciones", Toast.LENGTH_SHORT).show();
                    myvm.setFieldValue();
                    hideLoadingElement();
                }
            }
        });


    }

    @Override
    public void onPause() {
        Log.i("@@@@ onPause", "Show info");
        super.onPause();
        linearLayout.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
    }

    public void onStop() {
        Log.i("@@@@ onStop", "Show info");
        super.onStop();
    }
}
