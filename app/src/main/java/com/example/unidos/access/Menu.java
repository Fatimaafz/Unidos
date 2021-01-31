package com.example.unidos.access;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.accountManagement.AccountManagement;
import com.example.unidos.databinding.ActivityMenuBinding;
import com.example.unidos.searching.Search;

public class Menu extends AppCompatActivity {
    Intent intent = new Intent();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMenuBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        MenuViewModel menuViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(MenuViewModel.class);
        binding.setMenuvariable(menuViewModel);
        binding.setLifecycleOwner(this);
        PersistentData persistentData = new PersistentData();

        if(new PersistentData(this).checkExistence2()) {
            Log.i("^^^{ ", "USER IS LOGGED");
            menuViewModel.setCurp(persistentData.getCURPValue(this));
        }

        menuViewModel.changeScreen.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer activity) {
                if(activity == 1)
                    intent = new Intent(getApplicationContext(), Search.class);
                else
                    intent = new Intent(getApplicationContext(), AccountManagement.class);
                startActivity(intent);
            }
        });

    }

}
