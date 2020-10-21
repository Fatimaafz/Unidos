package com.example.unidos;
//change
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    FirebaseFirestore db;

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
    public ElementoObservable<Integer> userInfoExist;

    public UserRepository() {
        createInstance();
        insertStatusObservable = new ElementoObservable<>();
        checkPhoneExistenceObservable = new ElementoObservable<>();
        checkCurpExistenceObservable = new ElementoObservable<>();
        deleteUserObservable = new ElementoObservable<>();
        userUpdateObservable = new ElementoObservable();
        initObservable();
    }

    public void initObservable(){
        userInfoExist = new ElementoObservable<>();
    }

    public User getUser() {
        return user;
    }

    private void createInstance(){ /** To get a reference of the database. **/
        db =  FirebaseFirestore.getInstance();
    }

    public ElementoObservable getUserInfo(){ return userInfoExist; }

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
        db.collection("user")
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
                                checkPhoneExistenceObservable.setElemento(1);
                            }else{
                                /** Update the observable value so that
                                 * the observer can execute an action
                                 * -1 indicates the phone is registered. **/
                                checkPhoneExistenceObservable.setElemento(-1);
                            }
                        } else {
                            /** An error happened during the searching. **/
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
        u.put("firstName", user.getFirstName());
        u.put("surname1", user.getSurname1());
        u.put("dateBirth", user.getDateBirth());
        u.put("sex", user.getSex());
        u.put("phoneNumber", user.getTelephNumber());

        /** To know whether secondName or surname2
         * were provided and if not,
         * skip the values from the map. */
        if (!(user.getSecondName() == null))
            u.put("secondName", user.getSecondName());
        if (!(user.getSurname2() == null))
            u.put("surname2", user.getSurname2());

        /** Reference to the location in which
         * we need to search, in this case
         * Firebase will write a new document. **/
        db.collection("user").document(user.getCURP()).set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        final DocumentReference docRef = db.collection("user").document(curp);
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
                        Log.i("***User data: ", document.getData().toString());
                        Log.i("***value: ", document.get("firstName").toString());
                        user = document.toObject(User.class);
                        user.setCURP(curp);


                        Log.i("///", "REPOSITORY");

                        userInfoExist.setElemento(1);
                    } else {
                        /** Update the observable value so that
                         * the observer can execute an action
                         * 1 indicates the CURP is not registered. **/
                        userInfoExist.setElemento(0);
                    }
                } else {
                    /** An error happened during the searching. **/
                    userInfoExist.setElemento(-1);
                }
            }
        });
    }

    public void updateUserInfo(Map<String, Object> map){
        Log.i("@@@@ {{{{{{{{", "UPDATE METHOD!!!");
        DocumentReference ref = db.collection("user").document(map.get("curp").toString());

        if(!map.containsKey("surname2"))
            map.put("surname2", FieldValue.delete());

        if(!map.containsKey("secondName"))
            map.put("secondName", FieldValue.delete());

        ref.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("@@@", "success");
                userUpdateObservable.setElemento(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("@@@", "fail");
                userUpdateObservable.setElemento(false);
            }
        });
    }


    public void deleteUser(String curp){
        db.collection("user").document(curp)
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
}

