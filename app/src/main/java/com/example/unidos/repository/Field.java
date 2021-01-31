package com.example.unidos.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Field<T> {
    private String fieldID="";
    private T value;
    private T value2;

    public Field(String fieldID, T value){
        this.fieldID = fieldID;
        this.value = value;
    }

    public Field(){}

    public Field(T value, T value2){
        this.value = value;
        this.value2 = value2;
    }

    public T getValue(){
        return value;
    }

    public void setValue(T value){
        this.value = value;
    }

    public String getFieldID() {
        return fieldID;
    }

    public T getValue2() {
        return value2;
    }

    public void setValue2(T value2) {
        this.value2 = value2;
    }

    public void setFieldID(String fieldID) {
        this.fieldID = fieldID;
    }

    public int sexToInt(String value){
        if (value.equals("Mujer"))
            return 0;
        else
            return 1;
    }

    public int placeToInt(String value){
        if (value.equals("Cuauhtémoc"))
            return 15;
        else
            return 5;
    }

    public boolean statusToBool(String value){
        if (value.equals("Localizado"))
            return true;
        else
            return false;
    }

    public String intToSex(int sex){
        switch (sex){
            case 0:
                return "Mujer";
            case 1:
                return "Hombre";
            default: return " ";
        }
    }

    public String intToPlace(int place){
        switch (place){
            case 5:
                return "Gustavo A. Madero";
            case 15:
                return "Cuauhtémoc";
            default: return " ";
        }
    }

    public String boolToStatus(boolean status){
        if (status)
            return "Localizado";
        else
            return "No localizado";
    }

    public String compToString(int comp){
        switch (comp){
            case 1: return "Delgada";
            case 2: return "Mediana";
            case 3: return "Robusta";
            default: return " ";
        }
    }

    public String eyeColorToString(int color){
        switch (color){
            case 1: return "Azul";
            case 2: return "Café";
            case 3: return "Gris";
            case 4: return "Miel";
            case 5: return "Verde";
            default: return " ";
        }
    }

    public String eyeShapeToString(int eyeShape){
        switch (eyeShape){
            case 1: return "Almendrados";
            case 2: return "Caídos";
            case 3: return "Grandes";
            case 4: return "Hundidos";
            case 5: return "Juntos";
            case 6: return "Pequeños";
            case 7: return "Prominentes";
            case 8: return "Rasgados";
            case 9: return "Separados";
            default: return " ";
        }
    }

    public String faceToString(int faceShape){
        switch (faceShape){
            case 1: return "Cuadrada";
            case 2: return "Ovalada";
            case 3: return "Redonda";
            default: return " ";
        }
    }

    public String hairColorToString(int color){
        switch (color){
            case 1: return "Amarillo";
            case 2: return "Azul";
            case 3: return "Marrón";
            case 4: return "Morado";
            case 5: return "Negro";
            case 6: return "Pelirrojo";
            case 7: return "Rosa";
            case 8: return "Rubio";
            case 9: return "Verde";
            default: return " ";
        }
    }

    public String hairShapeToString(int shape){
        switch (shape){
            case 1: return "Lacio";
            case 2: return "Ondulado";
            case 3: return "Rizado";
            case 4: return "Sin cabello";
            default: return " ";
        }
    }

    public String lipsToString(int shape){
        switch (shape){
            case 1: return "Delgados";
            case 2: return "Gruesos";
            case 3: return "Medios";
            default: return " ";
        }
    }

    public String noseToString(int shape){
        switch (shape){
            case 1: return "Aguileña";
            case 2: return "Chata";
            case 3: return "Grande";
            case 4: return "Pequeña";
            case 5: return "Respingada";
            default: return " ";
        }
    }

    public String skinToString(int color){
        switch (color){
            case 1: return "Clara";
            case 2: return "Morena clara";
            case 3: return "Morena oscura";
            case 4: return "Oscura";
            default: return " ";
        }
    }

    public String dateToSimpleDate(Date date){
        if(date == null)
            return "-";
        else{
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.format(date);
        }
    }

    public Date getDateFromString(String datetoSaved){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = format.parse(datetoSaved);
            return date ;
        } catch (ParseException e){
            return null ;
        }

    }

    public boolean isFieldEmpty(String value){
        if(value == null)
            return true;
        else if(value.isEmpty())
            return true;
        else return false;
    }

    public boolean isSyntaxCorrect(String s, String regex){
        if(s.matches(regex)){
            return true;
        }else
            return false;
    }


    public int validateObligatoryField(String data, String regex){
        if(data == null)
            return 0;
        else if(data.isEmpty())
            return 0;
        else if(!data.matches(regex))
            return -1;
        else return 1;
    }

    public int validateNonObligatoryField(String data, String regex){
        if(data != null) {
            if (!data.matches(regex))
                return -1;
            else return 1;
        }else return 1;
    }

    public boolean enableFlag(int status){
        if(status == 1)
            return true;
        else return false;
    }


    public boolean checkFieldStatus(List<Boolean> list, boolean validField){
        if(validField) {
            for (Boolean bool : list) {
                if (!bool)
                    return false;
            }
        }else
            return false;
        return true;
    }

}
