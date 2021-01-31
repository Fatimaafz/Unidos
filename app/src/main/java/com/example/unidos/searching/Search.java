package com.example.unidos.searching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.databinding.ActivitySearchBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Search extends AppCompatActivity {

    private ViewPagerAdapter adapter;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DialogFragment dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        MoldViewModel myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(MoldViewModel.class);
        binding.setVmSearch(myvm);
        binding.setLifecycleOwner(this);

        SearchViewModel sharedViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        Log.i("^^^{", "Im the activity");
        adapter = new ViewPagerAdapter(this);
        viewPager2 = findViewById(R.id.pagerSearch);
        viewPager2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayoutSearch);

        myvm.getShowFilters().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    dialog = new FiltersDialog();
                    dialog.show(getSupportFragmentManager(),"Filtros");
                }
            }
        });

        sharedViewModel.getDismissDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hide) {
                if(hide)
                    dialog.dismiss();
            }
        });


        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_format_list_bulleted_24px);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_map_24px);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                    Log.i("@@@@@", "Tab 0");
                else if(tab.getPosition() == 1)
                    Log.i("@@@@@", "Tab 1");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, com.example.unidos.access.Menu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
