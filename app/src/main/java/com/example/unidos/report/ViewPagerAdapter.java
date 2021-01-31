package com.example.unidos.report;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new PersonDetails();
        else if(position == 1)
            return new ReportList();
        else if (position == 2) {
            NewReport rep = new NewReport();
            return rep;
        }else return new PersonDetails();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

