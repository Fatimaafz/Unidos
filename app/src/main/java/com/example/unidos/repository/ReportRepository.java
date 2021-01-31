package com.example.unidos.repository;

import android.location.Geocoder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportRepository extends ViewModel {
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private ElementoObservable<Integer> opResult;
    private ElementoObservable<Boolean> infoExist;
    private List<Report> reportList;

    public ReportRepository(){
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("report");
        opResult = new ElementoObservable<>();
        infoExist = new ElementoObservable<>();
        reportList = new ArrayList<>();
    }

    public ElementoObservable<Integer> getOpResult() {
        return opResult;
    }

    public ElementoObservable<Boolean> getInfoExist() {
        return infoExist;
    }

    public List<Report> getReportList() {
        return reportList;
    }

    public void findReports(final String personID, Date firstSeenDate) {
        Log.i("###### CURP", personID);
        Log.i("###### DATE", firstSeenDate.toString());
        collectionReference.whereGreaterThanOrEqualTo("seenDate", firstSeenDate).whereEqualTo("personCURP", personID)
                .orderBy("seenDate", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                opResult.setElemento(-2);
                            }else {
                                for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                    reportList.add(doc.toObject(Report.class));
                                }
                                opResult.setElemento(1);
                            }
                        }else {
                            opResult.setElemento(-1);
                        }
                    }
                });
    }

    public void generateReport(Map<String, Object> reportInfo){
        collectionReference.document().set(reportInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    opResult.setElemento(1);
                else
                    opResult.setElemento(-1);
            }
        });
    }

}
