package com.example.unidos.searching;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.Person;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unidos.Connection;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentSearchOnMapBinding;
import com.example.unidos.report.ReportContainer;
import com.example.unidos.repository.Report;
import com.example.unidos.repository.ReportedPerson;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchOnMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap map;
    private SupportMapFragment supportMapFragment;
    private SearchOnMapViewModel myvm;
    private Connection connection;
    private SearchViewModel searchViewModel;
    private boolean isFiltering;
    private List<ReportedPerson> reportedPeople = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SearchOnMapViewModel.class);
        connection = new Connection(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        searchViewModel.setIsInList(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSearchOnMapBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_on_map, container, false);
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmMap(myvm);

        Log.i("^^^{", "IN MAP");

        searchViewModel.getWantToGetLocation().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean want) {
                Log.i("######", "I want the location");
                if(want) {
                    isFiltering = true;
                    getLocation();
                }
            }
        });

        myvm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result){
                    case 1:
                        reportedPeople = myvm.getReportedPersonList();
                        map.clear();
                        showCoincidences();
                        break;
                    case -1:
                        showToast(Messages.ERR_MSSG_01);
                        break;
                    case -2:
                        showToast(Messages.ERR_MSSG_02);
                }
            }
        });


        searchViewModel.getCoincidencesForMap().observe(getViewLifecycleOwner(), new Observer<List<ReportedPerson>>() {
            @Override
            public void onChanged(List<ReportedPerson> list) {
                if(list != null) {
                    if(!list.isEmpty()) {
                        Log.i("^^^{ MAP size", String.valueOf(list.size()));
                        reportedPeople = list;
                        map.clear();
                        showCoincidences();
                    }
                }else {
                    Log.i("^^^{ MAP", "List IS  empty");
                    //map.clear();
                }
            }
        });

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    private void showCoincidences(){
        Log.i("###### MAP", "Showing coincidences");
        /*Location loc = new Location("loc1");
        Location loc2 = new Location("loc2");
        // oxxo 19.31905117355098, -99.11056897492223
        loc.setLatitude(19.31905117355098);
        loc.setLongitude(-99.11056897492223);

        //uniformes 19.31798554479503, -99.10987160057394
        loc2.setLatitude(19.31798554479503);
        loc2.setLongitude(-99.10987160057394);

        List<ReportedPerson> list2 = new ArrayList<>();
        ReportedPerson p = new ReportedPerson();
        p.setCurp("Fátima");
        p.setLocation(loc);

        ReportedPerson p2 = new ReportedPerson();
        p2.setCurp("Tronquito");
        p2.setLocation(loc2);
        list2.add(p);
        list2.add(p2);*/
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -15);
        Date lastFifteenDays = c.getTime();

        int position = 0;

        for(ReportedPerson person : reportedPeople){
            Log.i("######", "Im adding markers");
            Log.i("###### *curp", person.getCurp());

            map.addMarker(new MarkerOptions()
                    .title(person.getCurp())
                    .snippet(String.valueOf(position))
                    .icon(BitmapDescriptorFactory.defaultMarker(changeMarkerColor(
                            person.getRecSeenDate(), lastFifteenDays, person.isFound()
                    )))
            .position(new LatLng(person.getRecLocation().getLatitude(), person.getRecLocation().getLongitude())
            ));
            position++;
        }
    }

    private float changeMarkerColor(Date recSeenDate, Date lastFifteenDays, Boolean isFound){
        if(isFound)
            return BitmapDescriptorFactory.HUE_GREEN;
        else if(recSeenDate.compareTo(lastFifteenDays) >= 0)
            return BitmapDescriptorFactory.HUE_RED;
        else
            return BitmapDescriptorFactory.HUE_ORANGE;
    }

    @Override
    public void onMapReady(GoogleMap gmap) {
        map = gmap;
        initMap();
        map.setOnMarkerClickListener(this);
        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.i("######", "I clicked the marker");
        Intent intent = new Intent(getContext(), ReportContainer.class);
        intent.putExtra("SelPerson", new Gson().toJson(reportedPeople.get(
                Integer.parseInt(marker.getSnippet())
        )));
        startActivity(intent);
        return true;
    }

    private void getRecentReports(LatLng location){
        if(connection.isConnected())
            //Log.i("^^^{ MAP", "show recent reports");
            myvm.getRecentReports(location);
        else
            showToast(Messages.ERR_MSSG_00);
    }

    private void getLocation(){
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("######", "We got the location");
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                if (map != null) {
                                    Log.i("######", "Lets move the camera");
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),20));
                                    if(isFiltering) {
                                        Log.i("######", "Lets send user location");
                                        searchViewModel.setUserLocation(
                                                new LatLng(location.getLatitude(), location.getLongitude())
                                        );
                                        //searchViewModel.setTest(true);
                                        isFiltering = false;
                                    }else {
                                        Log.i("######", "Lets query");
                                        getRecentReports(
                                                new LatLng(location.getLatitude(), location.getLongitude())
                                        );
                                    }
                                }
                            }else {
                                isFiltering = false;
                                showToast(Messages.MSG007_2_LOCATION_FAIL);
                                Log.i("######", "Loc is null");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("######", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) { e.printStackTrace(); }
    }

    private void initMap(){
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.3781,-3.4360),10));
        if (checkPermission()) {
            Log.i("######","Lets get user location");
            //or move map to user location. - this is defined in next step
            getLocation();
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Log.i("######", "I click location btn");
                    isFiltering = false;
                    getLocation();
                    return true;
                }
            });
        } else {
            Log.i("######","Request permission");
            requestPermission();
        }
    }

    private void requestPermission() {
        requestPermissions(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, 44);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.i("######","On req perm result");
        switch (requestCode) {
            case 44:
                Log.i("######","Case 4");
                if (grantResults.length > 0) {
                    boolean finelocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarselocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (finelocation && coarselocation) {
                        if (checkPermission()) {
                            Log.i("######","check permission");
                            if (map != null) {
                                initMap();
                            }

                        }else
                            Log.i("######","permission rejected");
                    } else {
                        Log.i("######","We dont have perm");
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        Log.i("######","check permission");
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}
