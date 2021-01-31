package com.example.unidos.repository;

import android.util.Log;

public class User {
    /** User attributes **/
    private String CURP;
    private String name;
    private String name2;
    private String surname;
    private String firstName; //DELETE
    private String secondName; //DELETE
    private String surname1; //DELETE
    private String surname2;
    private String birthDate;
    private String dateBirth; //DELETE
    private String sex;
    private String phoneNumber;
    private String telephNumber;
    private String location;

    public User(){}

    /** Constructor **/
    public User(String curp, String firstName, String secondName, String surname1, String surname2, String dateBirth, String sex, String telephNumber){
        this.CURP=curp;
        this.firstName=firstName;
        this.secondName=secondName;
        this.surname1=surname1;
        this.surname2= surname2;
        this.dateBirth=dateBirth;
        this.sex=sex;
        this.telephNumber=telephNumber;
    }

    /** Attributes getters. **/
    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTelephNumber() {
        return telephNumber;
    }

    public void setTelephNumber(String telephNumber) {
        this.telephNumber = telephNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(String dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
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
                fullName += name;

                if (name2 != null)
                    fullName += "  " + name2;

                fullName += "\n" + surname;

                if (surname2 != null)
                    fullName += "  " + surname2;

                return fullName;
            }
    }

    public String getInitialUserInfo(){
        String initInfo = name + surname + birthDate + sex + phoneNumber+ name2 + surname2;
        Log.i("######", "get initial user info1");
        return initInfo;
    }
}
