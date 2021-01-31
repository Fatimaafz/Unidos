package com.example.unidos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.unidos.report.ReportContainer;
import com.example.unidos.repository.ReportedPerson;
import com.google.gson.Gson;

import java.util.Calendar;

public class ClickActionHelper {
    /*public static void startActivity(Context context){
        ReportedPerson person = new ReportedPerson();
        person.setName("Juana");
        person.setName2("La Cubana");
        person.setFound(false);
        person.setMissingPlace(15);
        person.setRecSeenDate(Calendar.getInstance().getTime());


        Intent i = new Intent(context, ReportContainer.class);
        i.putExtra("SelPerson", new Gson().toJson(person));
        //i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(i);
    }*/

    public static void startActivity(Context context){

        ReportedPerson person = new ReportedPerson();
        person.setName("Juana");
        person.setName2("La Cubana");
        person.setFound(false);
        person.setMissingPlace(15);
        person.setRecSeenDate(Calendar.getInstance().getTime());

        Intent i = new Intent(context, ReportContainer.class);
        i.putExtra("SelPerson", new Gson().toJson(person));
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(i);

    }
}