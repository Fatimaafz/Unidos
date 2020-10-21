package com.example.unidos.access;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.R;
import com.example.unidos.databinding.ActivityMenuBinding;

public class Menu extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMenuBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        MenuViewModel menuViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(MenuViewModel.class);
        binding.setMenuvariable(menuViewModel);
        binding.setLifecycleOwner(this);

        menuViewModel.setMenuContext(this);
    }

}
