package com.example.unidos.report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.unidos.Login;
import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.searching.Search;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

public class ReportContainer extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ReportSharedViewModel myvm;
    private ReportedPerson person;
    private int oldTab = 0, currentItem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);



        /*ActivityContainerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_container);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ReportSharedViewModel.class);
        binding.setVmShared(myvm);
        binding.setLifecycleOwner(this);*/

        myvm = new ViewModelProvider(this).get(ReportSharedViewModel.class);

        adapter = new ViewPagerAdapter(this);
        viewPager2 = findViewById(R.id.pagerReport);
        viewPager2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayoutReport);

        determineActivityToStart();



        myvm.getRepGenerated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean generated) {
                if(generated)
                    viewPager2.setCurrentItem(1);
            }
        });

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.i(",,,", "On config tab");

                /*if(position == 0)
                    tab.setText("Información");
                else if(position == 1)
                    tab.setText("Reportes");
                else if (position==2)
                    tab.setText("Reportar");*/
            }
        }).attach();

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_black_male_user_symbol);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_folder_24px);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_add_24px);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i(",,,", "On tab selected");
                Log.i(",,, OLD TAB", String.valueOf(oldTab));
                if (oldTab == 2 && tab.getPosition() < 2) {
                    currentItem = tab.getPosition();
                    Log.i(",,,", "Im in 2");
                    myvm.setChangeTab(true);
                } else {
                    Log.i(",,,", "Im in other tab");
                    oldTab = tab.getPosition();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    private void goToLogin(Bundle bundle){
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void determineActivityToStart(){
        Log.i(",,,", "Determine act to start");
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Log.i("^^^{ ", "EXTRAS NOT NULL");
            if (bundle.containsKey("log")) {
                Log.i("^^^{ in determact FR", "Go to login");
                goToLogin(bundle);
            }else{
                Log.i("^^^{ ", "dont go to login");
                person = new Gson().fromJson(getIntent().getStringExtra("SelPerson"), ReportedPerson.class);
                myvm.setPersonReceived(person);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, com.example.unidos.searching.Search.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
