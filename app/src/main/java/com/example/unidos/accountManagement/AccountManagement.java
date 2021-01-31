package com.example.unidos.accountManagement;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unidos.R;

public class AccountManagement extends AppCompatActivity {
    public static FragmentManager fragmentManager;

//changeq
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        /*ActivityAccountManagementBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_account_management);
        final AccountManagementViewModel accountManagementViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(AccountManagementViewModel.class);

        binding.setVm(accountManagementViewModel);

        binding.setLifecycleOwner(this);*/

        Log.i("-->><<--", "Im the activity");

        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
            }
            Log.i("***ACCMANAG", "ACCMANAG");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AccountManagementFragment accountManagementFragment = new AccountManagementFragment();
            fragmentTransaction.add(R.id.fragment_container, accountManagementFragment, null);
            fragmentTransaction.commit();
        }
    }
}
