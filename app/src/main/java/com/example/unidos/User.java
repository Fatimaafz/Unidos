package com.example.unidos;
//changeq

public class User {
    /** User attributes **/
    private String CURP;
    private String firstName;
    private String secondName;
    private String surname1;
    private String surname2;
    private String dateBirth;
    private String sex;
    private String phoneNumber;
    private String telephNumber;
    private String location;

    public User(){}

    /** Constructor **/
    User(String curp, String firstName, String secondName, String surname1, String surname2, String dateBirth, String sex, String telephNumber){
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

    public String getFullName(){
        String fullName = "";
        fullName += firstName;

        if (secondName != null)
            fullName += "  " + secondName;

        fullName += "  " + surname1;

        if(surname2 != null)
            fullName += "  "+ surname2;

        return fullName;
    }

    public String getInitialUserInfo(){
        String initInfo = firstName + surname1 + dateBirth + sex + phoneNumber;

        if (secondName != null)
            initInfo += secondName;

        if(surname2 != null)
            initInfo += surname2;

        return initInfo;
    }
}
