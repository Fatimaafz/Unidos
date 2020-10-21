package com.example.unidos.accountManagement;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
//changeq


public class ManageFragmentAdapter extends FragmentStateAdapter {
    Fragment userInfo;

    public ManageFragmentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        /** Return a NEW fragment instance in createFragment(int) **/
        UserInfo userInfo;
        switch (position){
            case 0:
                userInfo = new UserInfo();
                return userInfo;
            case 1:
                return new EditUserInfo();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
