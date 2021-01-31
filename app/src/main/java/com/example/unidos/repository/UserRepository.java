package com.example.unidos.repository;
//change
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private FirebaseFirestore db;

    /** Observable variables with observers in other
     * classes will be able to observe. **/
    /** To know the result of the insertion. **/
    private ElementoObservable<Boolean> insertStatusObservable;
    /** To know the result of the phone searching. **/
    private ElementoObservable<Integer> checkPhoneExistenceObservable;
    /** To know the result of the curp searching. **/
    private ElementoObservable<Integer> checkCurpExistenceObservable;
    /** To observe data retrieving **/
    private ElementoObservable<Boolean> deleteUserObservable;
    private ElementoObservable<Boolean> userUpdateObservable;
    private User user;
    private ElementoObservable<Integer> opResult;
    private CollectionReference userCollection;

    public UserRepository() {
        createInstance();
        insertStatusObservable = new ElementoObservable<>();
        userCollection = db.collection(FieldConstant.COLLECTION_USER);
        checkPhoneExistenceObservable = new ElementoObservable<>();
        checkCurpExistenceObservable = new ElementoObservable<>();
        deleteUserObservable = new ElementoObservable<>();
        userUpdateObservable = new ElementoObservable();
        initObservable();
    }

    public void initObservable(){
        opResult = new ElementoObservable<>();
    }

    public User getUser() {
        return user;
    }

    private void createInstance(){ /** To get a reference of the database. **/
        db =  FirebaseFirestore.getInstance();
    }

    public ElementoObservable<Integer> getOpResult() {
        return opResult;
    }

    public ElementoObservable getUserUpdateObservable(){return userUpdateObservable;}

    public ElementoObservable getDeleteUserStatus(){ return deleteUserObservable;}

    public ElementoObservable getInsertStatus(){
        return insertStatusObservable;
    }

    public ElementoObservable getCurpExistence(){
        return checkCurpExistenceObservable;
    }

    public ElementoObservable getPhoneExistence(){
        return checkPhoneExistenceObservable;
    }

    /** Method to search the curp in the collection user **/
    public void readCURP(String curp){
        System.out.println("CURP EN REPOSITORY: "+ curp);
        /** Reference to the location in which we need to
         * search, in this case Firebase will read
         * every document id (CURP) in user collection
         * till found coincidences. */
        DocumentReference docRef = db.collection("user").document(curp);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                /** If firebase can perform
                 * the searching. **/
                if (task.isSuccessful()) {
                    /**  Contains data read from a
                     * document in the database **/
                    DocumentSnapshot document = task.getResult();
                    /** Does Firebase found a document
                     * with the specified id? **/
                    if (document.exists()) {
                        /**  Update the observable value
                         * so that the observer can execute an action **/
                        /**  -1 indicates the CURP
                         * is already registered. **/
                        checkCurpExistenceObservable.setElemento(-1);
                    } else {
                        /** Update the observable value so that
                         * the observer can execute an action
                         * 1 indicates the CURP is not registered. **/
                        checkCurpExistenceObservable.setElemento(1);
                    }
                } else {
                    /** An error happened during the searching. **/
                    checkCurpExistenceObservable.setElemento(-2);
                }
            }
        });
    }

    /** Method to search the phone
     * n the collection user **/
    public void readPhone(String telephNumber){
        /** Reference to the location in which we need
         * to search, in this case Firebase will read
         * every document in user collection in order
         * to know if a field phoneNumber matches
         * with the number specified. */
        userCollection
                .whereEqualTo("phoneNumber", telephNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        /** If firebase can perform
                         * the searching. **/
                        if (task.isSuccessful()) {
                            /** Did Firebase find results? **/
                            if(task.getResult().isEmpty()){
                                /** Update the observable value so that
                                 * the observer can execute an action
                                 * 1 indicates the phone is not registered. **/
                                opResult.setElemento(-2);
                                checkPhoneExistenceObservable.setElemento(1);
                            }else{
                                /** Update the observable value so that
                                 * the observer can execute an action
                                 * -1 indicates the phone is registered. **/
                                opResult.setElemento(1);
                                checkPhoneExistenceObservable.setElemento(-1);
                            }
                        } else {
                            /** An error happened during the searching. **/
                            opResult.setElemento(-1);
                            checkPhoneExistenceObservable.setElemento(-2);
                        }
                    }
                });
    }

    /** Method to write a new document
     * in collection user. **/
    public void insert(User user) {
        /** The data must be passed to Firebase
         * on a key-value structure. **/
        Map<String, String> u = new HashMap<>();
        u.put("name", user.getFirstName());
        u.put("surname", user.getSurname1());
        u.put("birthDate", user.getDateBirth());
        u.put("sex", user.getSex());
        u.put("phoneNumber", user.getTelephNumber());

        /** To know whether secondName or surname2
         * were provided and if not,
         * skip the values from the map. */
        if (!(user.getSecondName() == null))
            u.put("name2", user.getSecondName());
        if (!(user.getSurname2() == null))
            u.put("surname2", user.getSurname2());

        /** Reference to the location in which
         * we need to search, in this case
         * Firebase will write a new document. **/
        userCollection.document(user.getCURP()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            /** If firebase can perform the searching. **/
            public void onComplete(@NonNull Task<Void> task) {
                /** Did Firebase write
                 * correctly the data? **/
                if (task.isSuccessful()) {
                    Log.i("Firestore: ", "Inserción exitosa");
                    /** Update the observable value so that
                     * the observer can execute an action **/
                    /**  true indicates the insertion was successful.**/
                    insertStatusObservable.setElemento(true);
                }else {
                    /** An error happened during the searching. **/
                    insertStatusObservable.setElemento(false);
                }
            }
        });
    }

    public void retrieveUserData(final String curp){
        System.out.println("CURP EN REPOSITORY: "+ curp);
        /** Reference to the location in which we need to
         * search, in this case Firebase will read
         * every document id (CURP) in user collection
         * till found coincidences. */
        final DocumentReference docRef = userCollection.document(curp);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                /** If firebase can perform
                 * the searching. **/
                if (task.isSuccessful()) {
                    /**  Contains data read from a
                     * document in the database **/
                    DocumentSnapshot document = task.getResult();
                    /** Does Firebase found a document
                     * with the specified id? **/
                    if (document.exists()) {
                        /**  Update the observable value
                         * so that the observer can execute an action **/
                        /**  -1 indicates the CURP
                         * is already registered. **/
                        user = document.toObject(User.class);
                        user.setCURP(curp);
                        opResult.setElemento(1);
                    } else {
                        /** Update the observable value so that
                         * the observer can execute an action
                         * 1 indicates the CURP is not registered. **/
                        opResult.setElemento(-2);
                    }
                } else {
                    /** An error happened during the searching. **/
                    opResult.setElemento(-1);
                }
            }
        });
    }

    public void updateUserInfo(Map<String, Object> map, String curp){
        Log.i("@@@@ {{{{{{{{", "UPDATE METHOD!!!");
        DocumentReference ref = userCollection.document(curp);

        ref.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("@@@", "success");
                //opResult.setElemento(1);
                userUpdateObservable.setElemento(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("@@@", "fail");
                //opResult.setElemento(-1);
                userUpdateObservable.setElemento(false);
            }
        });
    }


    public void deleteUser(String curp){
        userCollection.document(curp)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteUserObservable.setElemento(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                deleteUserObservable.setElemento(false);
            }
        });
    }



    public void checkDataExistence(String fieldKey, String value){
        userCollection
                .whereEqualTo(fieldKey, value)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        /** If firebase can perform
                         * the searching. **/
                        if (task.isSuccessful()) {
                            /** Did Firebase find results? **/
                            if(task.getResult().isEmpty()){
                                /** Update the observable value so that
                                 * the observer can execute an action
                                 * 1 indicates the phone is not registered. **/
                                Log.i(",,,", "IS EMPTY");
                                opResult.setElemento(-2);
                                checkPhoneExistenceObservable.setElemento(1);
                            }else{
                                /** Update the observable value so that
                                 * the observer can execute an action
                                 * -1 indicates the phone is registered. **/
                                Log.i(",,,", "PHONE EXIST");
                                opResult.setElemento(1);
                                checkPhoneExistenceObservable.setElemento(-1);
                            }
                        } else {
                            /** An error happened during the searching. **/
                            Log.i(",,,", "ERROR");
                            opResult.setElemento(-1);
                            checkPhoneExistenceObservable.setElemento(-2);
                        }
                    }
                });
    }
}

