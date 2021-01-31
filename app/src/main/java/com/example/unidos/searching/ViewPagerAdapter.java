package com.example.unidos.searching;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.Locale;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
        Log.i("@@@@ ", "Pager adapter constructor");
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.i("@@@@ selected pos ", String.valueOf(position));
        if(position == 0)
            return new SearchFragment();
        else
            return new SearchOnMapFragment();
    }

    @Override
    public int getItemCount() {
        Log.i("@@@@ ", "getItemCount");
        return 2;
    }
}
