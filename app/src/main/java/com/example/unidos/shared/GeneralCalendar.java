package com.example.unidos.shared;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.example.unidos.ElementoObservable;
import com.example.unidos.R;

import java.util.Calendar;
import java.util.Date;

public class GeneralCalendar {
    private ElementoObservable<String> date;
    private Calendar calendar;
    private Context context;

    public GeneralCalendar(Context context){
        date = new ElementoObservable<>();
        calendar = Calendar.getInstance();
        this.context = context;
    }

    public ElementoObservable<String> getDate() {
        return date;
    }

    public DatePickerDialog setCalendar(int yearToMaxDate, int yearToMinDate){
        final Calendar c = Calendar.getInstance();
        /** get the day, month and year **/
        final int mYear = c.get(Calendar.YEAR), mMonth = c.get(Calendar.MONTH), mDay = c.get(Calendar.DAY_OF_MONTH);
        /** The listener indicate the user has finished selecting a date. **/
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                context, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                /** set a date right format to a variable. **/
                String selectedDate = "";
                if(dayOfMonth<10 && month<9) {
                    selectedDate = "0"+dayOfMonth + "/0" + (month+1) + "/" + year;
                }else if(dayOfMonth<9)
                    selectedDate = "0"+dayOfMonth + "/" + (month+1) + "/" + year;
                else if(month<9)
                    selectedDate = dayOfMonth + "/0" + (month+1) + "/" + year;
                else
                    selectedDate = ""+dayOfMonth + "/" + (month+1) + "/" + year;
                /** call to the method in order to pass the date selected on every click **/
                date.setElemento(selectedDate);
            }
        }, mYear, mMonth, mDay);

        /** Configure the date to display, the system wont allow register minors **/
        Date today= new Date();
        c.setTime(today);
        c.add(Calendar.YEAR, -yearToMaxDate);
        datePickerDialog.getDatePicker().setMaxDate(c.getTime().getTime());
        c.add(Calendar.YEAR, -yearToMinDate);
        datePickerDialog.getDatePicker().setMinDate(c.getTime().getTime());
        return datePickerDialog;
    }

}
