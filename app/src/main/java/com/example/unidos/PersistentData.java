package com.example.unidos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.example.unidos.access.Menu;

public class PersistentData {
    private SharedPreferences sharedPreferences, sp;
    private Context context;

    public PersistentData(){}

    public PersistentData(Context context){
        this.context = context;
        /** Returns a localized formatted string from the application package default string table. **/
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_curp), context.MODE_PRIVATE);
        /** the created file can only be accessed by the calling application. **/
        sp = context.getSharedPreferences("logged", Context.MODE_PRIVATE);
    }

    public void checkExistence(){
        /** Returns the preference value if it exists **/
        /** If exists, change activity. **/
        SharedPreferences sharedPreferences = context.getSharedPreferences("logged", Context.MODE_PRIVATE);
        String pref = String.valueOf(sharedPreferences.getBoolean("logged", false));
        Log.i("##>> check pref: ", pref);
        if(sp.getBoolean("logged", false)) {
            goToMainActivity();
            Log.i("##>>", "LOGGG");
        }

    }

    public void setValues(String curp){
        /** To modify values in a SharedPreferences object. **/
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor1= sp.edit();
        /** Set a String value in the preferences editor
         to be written back once apply() are called. */
        editor.putString(context.getString(R.string.user_curp), curp).apply();
        /** Set a Boolean value in the preferences editor
         to be written back once apply() are called. */
        editor1.putBoolean("logged", true).apply();
        SharedPreferences sharedPreferences = context.getSharedPreferences("logged", Context.MODE_PRIVATE);
        String pref = String.valueOf(sharedPreferences.getBoolean("logged", false));
        Log.i("##>> set v pref: ", pref);

        /** Change the activity **/
        goToMainActivity();
    }

    public void removePreferences(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferences.Editor editor2 = sp.edit();
        editor.remove(context.getString(R.string.user_curp)).apply();
        editor2.putBoolean("logged", false).apply();
        Intent i = new Intent(context, Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public String getCURPValue(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_curp), Context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.user_curp), null);
    }

    /** Change activity **/
    private void goToMainActivity(){
        Intent i = new Intent(context, Menu.class);
        context.startActivity(i);
    }
}
