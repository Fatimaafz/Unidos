package com.example.unidos.repository;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unidos.ElementoObservable;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

import java.security.acl.LastOwnerException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportedPersonRepository {
    private FirebaseFirestore db;
    ReportedPerson person = null;
    private CollectionReference collectionReference;
    private ElementoObservable<Integer> opResult;
    private List<ReportedPerson> personList = new ArrayList<>();
    private static final String collectionName = "reportedPerson";
    private static final String fieldDateName = FieldConstant.FIELD_DATE_ID;
    private boolean listPeople;
    private LatLng userLocation;

    public ReportedPersonRepository(){
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection(collectionName);
        opResult = new ElementoObservable<>();
    }

    public ReportedPerson getPerson() {
        return person;
    }

    public void setListPeople(boolean listPeople) {
        this.listPeople = listPeople;
        Log.i("###### is in list?", String.valueOf(listPeople));
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public List<ReportedPerson> getPersonList() {
        return personList;
    }

    public ElementoObservable<Integer> getOpResult() {
        return opResult;
    }

    public void getOnePerson(String curp){
        db.collection(collectionName).document(curp)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i("***User data: ", document.getData().toString());
                        Log.i("***value: ", document.get("name").toString());
                        person = document.toObject(ReportedPerson.class);
                        person.setCurp(document.getId());
                        opResult.setElemento(1);
                    } else {
                        opResult.setElemento(-2);
                    }
                } else {
                    opResult.setElemento(-1);
                }
            }
        });
    }

    public void getPersonInfo(){
        Log.i("-->><<--", "REPO");
        Date currentTime = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, -30);
        Date lastMonth = c.getTime();

        Log.i("@@@@@ C. date", currentTime.toString());
        Log.i("@@@@@ L. month", lastMonth.toString());

        Query query = db.collection(collectionName).whereGreaterThanOrEqualTo("recSeenDate", lastMonth)
                .whereLessThanOrEqualTo("recSeenDate", currentTime)
                .orderBy("recSeenDate", Query.Direction.DESCENDING);
        getInfo(query);
    }

    private void getInfo(Query query){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()) {
                        Log.i("######", "EMPTY");
                        opResult.setElemento(-2);
                    }else {
                        Log.i("######", "success");
                        if(listPeople)
                            convertDocToPerson(task);
                        else
                            convertDocToPersonForMap(task);
                    }
                }else {
                    Log.i("###### Fail", task.getException().toString());
                    opResult.setElemento(-1);
                }
            }
        });
    }

    private void convertDocToPerson(Task<QuerySnapshot> task){
        int position = 0;
        ReportedPerson reportedPerson;
        for(DocumentSnapshot doc : task.getResult().getDocuments()){
            Log.i("######", "for loop!!!!");
            reportedPerson = doc.toObject(ReportedPerson.class);
            reportedPerson.setCurp(doc.getId());
            if (reportedPerson.isFound()) {
                personList.add(position, reportedPerson);
                position++;
            } else
                personList.add(reportedPerson);
        }
        printList();
        opResult.setElemento(1);
    }

    private void printList(){
        for(ReportedPerson reportedPerson :  personList){
            Log.i("######", reportedPerson.getCurp());
        }
    }

    public Date getDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private Date getOldestDate(int oldestYear){
        Log.i("-->><<-- OLD", getDate(oldestYear, 1, 1).toString());
        return getDate(oldestYear, 1, 1);
    }

    private Date getLatestDate(int latestYear){
        Calendar calendar = Calendar.getInstance();
        if(latestYear == calendar.get(Calendar.YEAR)) {
            Log.i("-->><<-- NEW", calendar.getTime().toString());
            return calendar.getTime();
        }else {
            Log.i("-->><<-- NEW", getDate(latestYear, 12, 31).toString());
            return getDate(latestYear, 12, 31);
        }

    }

    public void filterByDate(List<Field> fieldList){
        Log.i("######", "FILTER BY DATE");
        Log.i("######", String.valueOf(getOldestDate((int)fieldList.get(0).getValue())));
        Log.i("######", String.valueOf(getOldestDate((int)fieldList.get(0).getValue2())));
        Query query = collectionReference.whereGreaterThanOrEqualTo(fieldDateName, getOldestDate((int)fieldList.get(0).getValue()))
                .whereLessThanOrEqualTo(fieldDateName, getLatestDate((int)fieldList.get(0).getValue2()))
                .orderBy(fieldDateName, Query.Direction.DESCENDING);
        getInfo(query);
    }

    public void applyOneFilter(List<Field> selectedFields){
        Log.i("######", "APPLY ONE");
        Query query = collectionReference.whereEqualTo(selectedFields.get(1).getFieldID(), selectedFields.get(1).getValue())
                .whereGreaterThanOrEqualTo(fieldDateName, getOldestDate((int)selectedFields.get(0).getValue()))
                .whereLessThanOrEqualTo(fieldDateName, getLatestDate((int)selectedFields.get(0).getValue2()))
                .orderBy(fieldDateName,Query.Direction.DESCENDING);

        getInfo(query);
    }

    public void applyTwoFilters(List<Field> selectedFields){
        Log.i("######", "APPLY TWO");
        Query query = collectionReference.whereEqualTo(selectedFields.get(1).getFieldID(), selectedFields.get(1).getValue())
                .whereEqualTo(selectedFields.get(2).getFieldID(), selectedFields.get(2).getValue())
                .whereGreaterThanOrEqualTo(fieldDateName, getOldestDate((int)selectedFields.get(0).getValue()))
                .whereLessThanOrEqualTo(fieldDateName, getLatestDate((int)selectedFields.get(0).getValue2()))
                .orderBy(fieldDateName,Query.Direction.DESCENDING);
        getInfo(query);
    }

    public void applyThreeFilters(List<Field> selectedFields){
        Log.i("######", "APPLY THREE");
        Query query = collectionReference.whereEqualTo(selectedFields.get(1).getFieldID(), selectedFields.get(1).getValue())
                .whereEqualTo(selectedFields.get(2).getFieldID(), selectedFields.get(2).getValue())
                .whereEqualTo(selectedFields.get(3).getFieldID(), selectedFields.get(3).getValue())
                .whereGreaterThanOrEqualTo(fieldDateName, getOldestDate((int)selectedFields.get(0).getValue()))
                .whereLessThanOrEqualTo(fieldDateName, getLatestDate((int)selectedFields.get(0).getValue2()))
                .orderBy(fieldDateName,Query.Direction.DESCENDING);
        getInfo(query);
    }

    public void applyFourFilters(List<Field> selectedFields){
        Log.i("######", "APPLY FOUR");
        Query query = collectionReference.whereEqualTo(selectedFields.get(1).getFieldID(), selectedFields.get(1).getValue())
                .whereEqualTo(selectedFields.get(2).getFieldID(), selectedFields.get(2).getValue())
                .whereEqualTo(selectedFields.get(3).getFieldID(), selectedFields.get(3).getValue())
                .whereEqualTo(selectedFields.get(4).getFieldID(), selectedFields.get(4).getValue())
                .whereGreaterThanOrEqualTo(fieldDateName, getOldestDate((int)selectedFields.get(0).getValue()))
                .whereLessThanOrEqualTo(fieldDateName, getLatestDate((int)selectedFields.get(0).getValue2()))
                .orderBy(fieldDateName,Query.Direction.DESCENDING);
        getInfo(query);
    }

    public void updateRecentSeenDate(Map<String, Object> attrMap, String personCURP){
        Log.i("######", "LETS update the value");
        collectionReference.document(personCURP)
                .update(attrMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                opResult.setElemento(1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(",,,p repo fail", e.getMessage());
                opResult.setElemento(-1);
            }
        });

           /*     .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("######", "I UPDATE the value");
                    opResult.setElemento(1);
                }else {
                    Log.i("######", "I COULDNT update the value");
                    opResult.setElemento(-1);
                }
            }
        });*/
    }

    private void convertDocToPersonForMap(Task<QuerySnapshot> task){
        ReportedPerson reportedPerson;
        for (DocumentSnapshot doc: task.getResult().getDocuments()){
            if(doc.contains("recLocation")) {
                if (isPersonNear(
                        new LatLng(((GeoPoint) doc.get("recLocation")).getLatitude(),
                                ((GeoPoint) doc.get("recLocation")).getLongitude()),
                        userLocation)) {

                    reportedPerson = doc.toObject(ReportedPerson.class);
                    reportedPerson.setCurp(doc.getId());
                    personList.add(reportedPerson);

                } else
                    Log.i("^^^{", "location field is null in for");
            }
        }

        if(personList.size() == 0 || personList == null) {
            Log.i("^^^{ repo", "LIST is empty");
            opResult.setElemento(-2);
        }else if (personList.size() > 0 ) {
            Log.i("^^^{", "list have elements");
            opResult.setElemento(1);
        }
        printList();

    }

    public boolean isPersonNear(LatLng personLoc, LatLng userLoc) {
        int rad = 6371;// radius of earth in Km
        double lat1 = personLoc.latitude;
        double lat2 = userLoc.latitude;
        double lon1 = personLoc.longitude;
        double lon2 = userLoc.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = rad * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        if(kmInDec <= 2) return true;
        else {
            opResult.setElemento(-3);
            return false;
        }
    }

}
