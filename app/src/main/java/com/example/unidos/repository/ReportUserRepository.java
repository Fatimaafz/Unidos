package com.example.unidos.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unidos.ElementoObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

public class ReportUserRepository {
    private FirebaseFirestore db;
    private CollectionReference reportUserCollection;
    private ElementoObservable<Integer> opResult;

    public ReportUserRepository(){
        db = FirebaseFirestore.getInstance();
        reportUserCollection = db.collection(FieldConstant.COLLECTION_REPORT_USER);
        opResult = new ElementoObservable<>();
    }

    public ElementoObservable<Integer> getOpResult() {
        return opResult;
    }

    private void executeTask(Task task){
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                opResult.setElemento(2);
            }
        }). addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("### F uo", e.getMessage());
                opResult.setElemento(-1);
            }
        });
    }

    public void deleteReportUser(String curp){
        executeTask(reportUserCollection.document(curp).delete());
    }

    public void updateUserInfoForReport(Map<String, Object> userInfo, String curp){
        executeTask(reportUserCollection.document(curp).update(userInfo));

    }

    public void createReportUser(Map<String, Object> userInfo, String curp){
        executeTask(reportUserCollection.document(curp).set(userInfo));
                /*
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            opResult.setElemento(2);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        opResult.setElemento(-1);
                    }
                });*/
    }

    public void checkDocumentExistence(String curp){
        reportUserCollection.document(curp)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                    opResult.setElemento(1);
                else
                    opResult.setElemento(-2);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        opResult.setElemento(-1);
                    }
                });


        /*        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists())
                        opResult.setElemento(1);
                    else
                        opResult.setElemento(-2);
                }else {
                    opResult.setElemento(-1);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                opResult.setElemento(-1);
            }
        });*/
    }

}
