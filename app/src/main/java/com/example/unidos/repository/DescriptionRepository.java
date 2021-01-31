package com.example.unidos.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unidos.ElementoObservable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DescriptionRepository {
    private FirebaseFirestore db;
    private Description description;
    private CollectionReference collectionReference;
    private ElementoObservable<Boolean> opResult;

    public DescriptionRepository(){
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("description");
        opResult = new ElementoObservable<>();
    }

    public Description getDescription() {
        return description;
    }

    public ElementoObservable<Boolean> getOpResult() {
        return opResult;
    }

    public void findDescription(String desc){
        collectionReference.document(desc).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Log.i("######", "success");
                    if(task.getResult().exists()){
                        description = task.getResult().toObject(Description.class);
                        opResult.setElemento(true);
                    }
                }
                else {
                    Log.i("###### fail", task.getException().toString());
                    opResult.setElemento(false);
                }
            }
        });
    }
}
