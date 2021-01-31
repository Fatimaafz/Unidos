package com.example.unidos.repository;

import android.location.Location;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;

import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.getInstance;

public class ReportedPerson{
    private String name, name2, surname, surname2;
    private Date firstSeenDate;
    private String photoPath;
    private Date birth;
    private Date recSeenDate;
    private String curp;
    private String nat = "";
    private boolean found;
    private int missingPlace;
    private int sex;
    private String descID;
    private List<String> reports;
    private String phone;
    private Location location;
    private GeoPoint recLocation;

    public ReportedPerson(){};

    public Date getFirstSeenDate() {
        return firstSeenDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Date getRecSeenDate() {
        return recSeenDate;
    }

    public void setRecSeenDate(Date recSeenDate) {
        this.recSeenDate = recSeenDate;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNat() {
        return nat;
    }

    public void setNat(String nat) {
        this.nat = nat;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public int getMissingPlace() {
        return missingPlace;
    }

    public void setMissingPlace(int missingPlace) {
        this.missingPlace = missingPlace;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getDescID() {
        return descID;
    }

    public void setDescID(String descID) {
        this.descID= descID;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public GeoPoint getRecLocation() {
        return recLocation;
    }

    public void setRecLocation(GeoPoint recLocation) {
        this.recLocation = recLocation;
    }

    private String capitalizaWord(String word){
        return (Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase());
    }

    public String getFullName(){
        String fullName = "";
        if(name == null && name2 == null
                && surname == null && surname2 == null)
            return "-";
        else
        if(name.isEmpty() && name2.isEmpty()
                && surname.isEmpty() && surname2.isEmpty())
            return "-";
        else {
            fullName += capitalizaWord(name);

            if (name2 != null)
                fullName += "  " + capitalizaWord(name2);

            fullName += "\n" + capitalizaWord(surname);

            if (surname2 != null)
                fullName += "  " + capitalizaWord(surname2);

            return fullName;
        }
    }

    public String getAge(){
        if(birth != null) {
            Calendar calendar = getInstance();
            Calendar calendar2 = getInstance();
            calendar2.setTime(birth);

            int age = calendar.get(Calendar.YEAR) - calendar2.get(Calendar.YEAR);
            if (calendar2.get(Calendar.DAY_OF_YEAR) < calendar.get(Calendar.DAY_OF_YEAR))
                return String.valueOf(age--);
            else return String.valueOf(age);
        }else return " ";
    }
}
