package com.example.unidos.report;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.unidos.R;
import com.example.unidos.databinding.FragmentShowReportDetailsBinding;
import com.example.unidos.repository.Report;

public class ReportDetailsDialog extends DialogFragment {
    private ReportDetailsDialogViewModel myvm;
    private ReportSharedViewModel sharedViewModel;
    private AlertDialog detailsDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ReportDetailsDialogViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReportSharedViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        FragmentShowReportDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_show_report_details, null, false);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        binding.setVmRepDet(myvm);
        builder.setView(view);
        detailsDialog = builder.create();
        Log.i("######", "Im in report dialog");

        sharedViewModel.getReport().observe(getViewLifecycleOwner(), new Observer<Report>() {
            @Override
            public void onChanged(Report report) {
                Log.i("######", "I received the report");
                myvm.showInfo(report);
            }
        });

        return view;
    }
}
