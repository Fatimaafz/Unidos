package com.example.unidos.shared;

//change
import java.util.List;

public class Field {
    private int numberOfField;
    private String nameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)+$";
    private String secondNameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)*$";
    private String curpRE= "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";
    private String phoneRE="[0-9]{10}";
    private String validationMsg = "";

    public void setNumberOfField(int numberOfField) {
        this.numberOfField = numberOfField;
    }

    public String getValidationMsg(){
        return validationMsg;
    }

    public boolean isNameWrong(String s){
       // Log.i("@@@@ n field", String.valueOf(numberOfField));
        if(numberOfField == 0 || numberOfField == 2){
            if (isFieldEmpty(s)){
                return true;
            }
            else if (s.matches(nameRE)) {
                validationMsg = "valid";
                return false;
            }else{
                validationMsg = "noNameMatch";
                return true;
            }
        } else if (s.matches(secondNameRE)){
            validationMsg = "valid";
            return false;
        }
        else{
            validationMsg = "noNameMatch";
            return true;
        }
    }

    public boolean checkFieldStatus(List<Boolean> list){
        boolean status = true;
        for (Boolean bool : list) {
            if (!bool)
                return false;
        }
        return status;
    }

    public boolean isFieldEmpty(String s){
        if(s == null){
            validationMsg = "empty";
            return true;
        }else if (s.isEmpty()){
            validationMsg = "empty";
            return true;
        }
        else return false;
    }

    public boolean isCURPwrong(String curp){
        if (isFieldEmpty(curp)) return true;
        else if (curp.matches(curpRE)){
            validationMsg = "valid";
            return false;
        }
        else{
            validationMsg = "noCURPmatch";
            return true;
        }
    }

    public boolean isPhoneWrong(String phone) {
        if (isFieldEmpty(phone))return true;
        else if (phone.matches(phoneRE)){
            validationMsg = "valid";
            return false;
        }
        else{
            validationMsg = "noPhoneMatch";
            return true;
        }
    }

    private boolean fieldMatchRE(String s, String regularExpresion){
        if (s.matches(regularExpresion)){
            validationMsg = "valid";
            return true;
        }
        else {
            validationMsg = "noNameMatch";
            return false;
        }
    }

}