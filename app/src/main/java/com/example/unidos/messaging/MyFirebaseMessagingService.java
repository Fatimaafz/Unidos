package com.example.unidos.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.unidos.ClickActionHelper;
import com.example.unidos.ElementoObservable;
import com.example.unidos.Messages;
import com.example.unidos.PersistentData;
import com.example.unidos.R;
import com.example.unidos.report.ReportContainer;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String title;
    private String body;
    private ReportedPerson person = null;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("^^^{ ", "I Received the notification");
        if(remoteMessage.getNotification() != null){
            Log.i("^^^{ ", "Notification is not null");
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
            if(remoteMessage.getData().size() > 0) {
                Log.i("^^^{ ", "notif size is > 0");
                if(isLogged()) {
                    Log.i("^^^{ ", "USR is  logged");
                    getUserLocation(new LatLng(Double.parseDouble(remoteMessage.getData().get("lat")),
                            Double.parseDouble(remoteMessage.getData().get("lon"))), remoteMessage.getData().get("CURP"));
                }else{
                    Log.i("^^^{ ", "USER is not log");
                    showNotification(buildPersonActForLog(remoteMessage.getData().get("CURP"),
                            remoteMessage.getData().get("lat"),
                            remoteMessage.getData().get("lon")));
                }
            }

        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("ch01", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Intent buildPersonActForLog(String curp, String lat, String lon){
        Intent intent = new Intent(this, ReportContainer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("CURP", curp);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        intent.putExtra("log", "1");
        return intent;
    }

    private Intent buildPersonAct(){
        Intent intent = new Intent(this, ReportContainer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("SelPerson", new Gson().toJson(person));
        return intent;
    }

    private void getPersonInfo(LatLng personLoc, LatLng userLoc, String curp){
        final ReportedPersonRepository repository = new ReportedPersonRepository();

        if(repository.isPersonNear(personLoc, userLoc)) {
            repository.getOpResult().addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    if ((int) ((ElementoObservable) o).getElemento() == 1) {
                        Log.i("^^^{ ", "I have person info");
                        person = repository.getPerson();
                        showNotification(buildPersonAct());
                    } else
                        Log.i("^^^{ ", "Error getting person info");
                }
            });
        }
        repository.getOnePerson(curp);
    }

    private void getUserLocation(final LatLng personLatLon, final String curp){
        FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            locationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // GPS location can be null if GPS is switched off
                            if (location != null) {
                                Log.i("^^^{  ", "We got the location");
                                getPersonInfo(personLatLon, new LatLng(location.getLatitude(), location.getLongitude()), curp);
                            }else {
                                showToast(Messages.MSG007_2_LOCATION_FAIL);
                                Log.i("^^^{ ", "Loc is null");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("^^^{ ", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        } catch (SecurityException e) { e.printStackTrace(); }
    }

    private void showNotification(Intent intent){
        Log.i("^^^{" , "lets show the notif");
        /*ReportedPerson person = new ReportedPerson();
        person.setName("Juana");
        person.setName2("La Cubana");
        person.setFound(false);
        person.setMissingPlace(15);
        person.setRecSeenDate(Calendar.getInstance().getTime());*/
        //Log.i("^^^{ NAME" , person.getName());

        //startActivity(intent);

        createNotificationChannel();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "ch01")
                .setSmallIcon(R.drawable.ic_notificationicon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(2)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService((Context.NOTIFICATION_SERVICE));
        notificationManager.notify(0, notificationBuilder.build());
    }



    private boolean isLogged(){
        if(new PersistentData(this).checkExistence2()) {
            Log.i("^^^{ ", "USER IS LOGGED");
            return true;
        }else
            return false;
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
