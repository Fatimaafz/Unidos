package com.example.unidos;

import android.util.Log;

import java.util.Observable;

/**
 *  An observable element is class which will point
 *  to a property we want to observe.
 * */
public class ElementoObservable<T> extends Observable { //I ADD PUBLIC

    private T elemento;
    /**
     *  This get will allow access to the current value of the element
     * */
    public T getElemento(){
        return elemento;
    }
    /**
     *  This set will change the value of the element and will notify
     *  its observers that there has been an update
     * */
    public void setElemento(T elemento) {
        this.elemento = elemento;
        Log.d("--->", "setElemento: " + elemento.toString());
        setChanged();   // Este método debe ejecutarse antes del notify
        notifyObservers();  // Se notifica del cambio a los observadores
    }

    /**
     * This method allows you to initialize the element property without
     * having to notify your watchers that they might have a change
     * */
    public void adherirElemento(T elemento) {
        this.elemento = elemento;
    }
}