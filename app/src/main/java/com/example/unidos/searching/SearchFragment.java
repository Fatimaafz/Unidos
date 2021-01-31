package com.example.unidos.searching;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unidos.Connection;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentSearchBinding;
import com.example.unidos.report.ReportContainer;
import com.example.unidos.repository.ReportedPerson;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchInListViewModel myvm;
    private RecyclerView recyclerView = null;
    private PersonAdapter adapter;
    private SearchViewModel searchViewModel;
    private Connection connection;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SearchInListViewModel.class);
        connection = new Connection(getContext());

        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addressList = geocoder.getFromLocationName("México, Ciudad de México, " +
                    "Fraccionamiento Popular Emiliano Zapata, Coyoacán, 75, Hacienda Chinameca", 1);
            Log.i("###### LAT: ", String.valueOf(addressList.get(0).getLatitude()));
            Log.i("###### LON: ", String.valueOf(addressList.get(0).getLongitude()));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        searchViewModel.setIsInList(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSearchBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        final View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmListSearch(myvm);

        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        recyclerView = view.findViewById(R.id.rvListPeople);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        Log.i("^^^{", "IN LIST");

        /*ReportedPerson person = new ReportedPerson();
        person.setName("Juana");
        person.setName2("La Cubana");
        person.setFound(false);
        person.setMissingPlace(15);
        person.setRecSeenDate(Calendar.getInstance().getTime());

        List<ReportedPerson> reportedPersonList = new ArrayList<>();
        reportedPersonList.add(person);
        showList(reportedPersonList);
        showList(reportedPersonList);*/

        myvm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        showList(myvm.getReportedPersonList());
                        break;
                    case -1:
                        showToast(Messages.ERR_MSSG_01);
                        break;
                    case -2:
                        showToast(Messages.ERR_MSSG_02);
                }
            }
        });

        if(connection.isConnected())
            //Log.i("^^^{", "show recent reports");
            myvm.getRecentReports();
        else
            showToast(Messages.ERR_MSSG_00);

        searchViewModel.getCoincidences().observe(getViewLifecycleOwner(), new Observer<List<ReportedPerson>>() {
            @Override
            public void onChanged(List<ReportedPerson> reportedPeople) {
                Log.i("######", "I received the list of people");
                showList(reportedPeople);
            }
        });


        return view;
    }

    private void showList(List<ReportedPerson> reportedPeople){
        adapter = new PersonAdapter(reportedPeople);
        recyclerView.setAdapter(adapter);

        adapter.getIsPersonSelected().addObserver(new java.util.Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if((boolean) ((ElementoObservable) o).getElemento()) {
                    Intent intent = new Intent(getContext(), ReportContainer.class);
                    intent.putExtra("SelPerson", new Gson().toJson(adapter.getReportedPerson()));
                    startActivity(intent);
                }
            }
        });
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}
