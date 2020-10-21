package com.example.unidos.accountManagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.unidos.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
//secondchangeq
public class AccountManagementFragment  extends Fragment {
    private ManageFragmentAdapter manageFragmentAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private AccountManagementViewModel accountManagementViewModel;
    private int oldTab = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        accountManagementViewModel =  new ViewModelProvider(requireActivity()).get(AccountManagementViewModel.class);
        return inflater.inflate(R.layout.fragment_account_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        manageFragmentAdapter = new ManageFragmentAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(manageFragmentAdapter);
        tabLayout = view.findViewById(R.id.tab_layout);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (oldTab == 1 && tab.getPosition() == 0) {
                    viewPager.setCurrentItem(1);
                    requestConfirm();
                } else
                    oldTab = tab.getPosition();
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_person);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_edit);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.OnConfigureTabCallback() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if(position == 0)
                            tab.setIcon(R.drawable.ic_person);

                        if(position == 1) {
                            tab.setIcon(R.drawable.ic_edit);
                        }
                    }
                }

        ).attach();
    }

    public void requestConfirm(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialogTheme);
        builder.setMessage("No se actualizarán los datos. \n¿Deseas salir?")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        oldTab=0;
                        accountManagementViewModel.setChangeTab(true);
                        viewPager.setCurrentItem(0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        accountManagementViewModel.setChangeTab(false);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView tv = (TextView) alertDialog.findViewById(android.R.id.message);
        tv.setTypeface(ResourcesCompat.getFont(getContext(), R.font.ralewayregular));


    }

}
