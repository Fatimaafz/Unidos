package com.example.unidos.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentRepListBinding;
import com.example.unidos.repository.Report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

public class ReportList extends Fragment {
    private ReportListViewModel myvm;
    private ReportSharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private Connection connection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ReportListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRepListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rep_list, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmRepList(myvm);

        Log.i("-->><<--", "Im here!!!!");
        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReportSharedViewModel.class);
        recyclerView = view.findViewById(R.id.rvListReport);

        connection = new Connection(getContext());

        sharedViewModel.getPersonID().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("######", "Going to search reports" + s);
                Connection connection = new Connection(getContext());
                if(connection.isConnected())
                    myvm.searchReports(s, sharedViewModel.getFirstSeenDate());
                else
                    showToast(Messages.ERR_MSSG_00);
            }
        });
        sharedViewModel.setRepGenerated(true);

        sharedViewModel.getRepGenerated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean updateList) {
                Log.i("######", "i detect a report was generated in r list");
                if(updateList) {
                    if(connection.isConnected()) {
                        Log.i("######PERDON ID", sharedViewModel.getPersonID().getValue());
                        myvm.searchReports(sharedViewModel.getPersonID().getValue(), sharedViewModel.getFirstSeenDate());
                    }else
                        showToast(Messages.ERR_MSSG_00);
                }else
                    showToast(Messages.ERR_MSSG_01);
            }
        });

        /*final Report report = new Report();
        report.setElabDate(Calendar.getInstance().getTime());
        report.setReportType(1);
        report.setTown("GAM");
        report.setClothe("some ropa");
        report.setDetails("algunos detalles");
        report.setAddrDetail("una colonia, una calle");
        report.setLastDateSeen(Calendar.getInstance().getTime());
        List<Report> reportList = new ArrayList<>();
        reportList.add(report);
        showList(reportList);*/

        myvm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        Log.i("######", "Report found");
                        showList(myvm.getReportList());
                        break;
                    case -1:
                        Log.i("######", "ERROR");
                        showToast(Messages.ERR_MSSG_01);
                        break;
                    case -2:
                        Log.i("######", "ERROR");
                        showToast(Messages.ERR_MSSG_02);
                        break;
                }
            }
        });

        return view;
    }

    private void showList(final List<Report> reportList){
        Log.i("######LIST SIZE", String.valueOf(reportList.size()));
        adapter = new ReportAdapter(reportList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);

        adapter.getReportSelected().addObserver(new java.util.Observer() {
            @Override
            public void update(Observable o, Object arg) {
                sharedViewModel.setReport(reportList.get((int) ((ElementoObservable) o).getElemento()));
                DialogFragment dialog = new ReportDetailsDialog();
                dialog.show(getParentFragmentManager(),"RepDet");
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        Log.i("######", "IM ON PAUSE");
        super.onPause();
    }
}
