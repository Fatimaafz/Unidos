package com.example.unidos.repository;

import java.util.Date;

public class Report {
    private int reportType;
    private Date elabDate;
    private String personID;
    private String userCURP;
    private String userPhone;
    private Date seenDate;
    private String town;
    private String clothe;
    private String details;
    private String addrDetail;
    private Date date;

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    public Date getElabDate() {
        return elabDate;
    }

    public void setElabDate(Date elabDate) {
        this.elabDate = elabDate;
    }

    public String getPersonID() {
        return personID;
    }

    public void setReportedPerson(String personID) {
        this.personID = personID;
    }

    public Date getSeenDate() {
        return seenDate;
    }

    public String getTown() {
        return town;
    }

    public String getClothe() {
        return clothe;
    }

    public String getDetails() {
        return details;
    }

    public void setLastDateSeen(Date seenDate) {
        this.seenDate = seenDate;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setClothe(String clothe) {
        this.clothe = clothe;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getUserCURP() {
        return userCURP;
    }

    public void setUserCURP(String userCURP) {
        this.userCURP = userCURP;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String repTypeToString(int type){
        switch (type){
            case -1: return "No localización";
            case 0: return "Desaparición";
            case 1: return "Avistamiento";
            default: return "-";
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
