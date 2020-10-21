package com.example.unidos;
//changeq
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import static android.net.NetworkCapabilities.TRANSPORT_CELLULAR;

public class Connection{
    Context context;
    /** Answers queries about the state of network connectivity. **/
    /** Also notifies applications when network connectivity changes. **/
    ConnectivityManager connectivityManager;

    /** a context is needed to get the system service **/
    public Connection(Context context){
        this.context=context;
        /** Return the handle to a system-level service by name. **/
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /*private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }*/

    /** Returns a Network object corresponding to the currently active default data network.
        Or null if no default network is currently active. **/
    public Network getNetwork(){
        return connectivityManager.getActiveNetwork();
    }

    /** To know if cellular is the current type of Internet connection. **/
    public boolean isTransportCellular(){
        return connectivityManager.getNetworkCapabilities(getNetwork()).hasTransport(TRANSPORT_CELLULAR);
    }

    /** To know if WiFi is the current type of Internet connection. **/
    public boolean isTransportWiFi(){
        return connectivityManager.getNetworkCapabilities(getNetwork()).hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
    }

    /** Is the phone connected? **/
    public boolean isNotConnected(){
        return (getNetwork() == null);
    }

    /** Was the connectivity on this network successfully validated? **/
    public boolean checkConnection(){
        System.out.println(connectivityManager.getActiveNetwork());
        return connectivityManager.getNetworkCapabilities(getNetwork()).hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }
}

/*


import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Handler;

public class Connection implements Runnable{
    Context context;
    int flag;
    Handler handler;
    private ElementoObservable<Integer> internetConnectionStatus;

    Connection(Context context){
        this.context=context;
        internetConnectionStatus= new ElementoObservable<>();
        internetConnectionStatus.adherirElemento(flag);
    }
    public void run(){
        hasActiveInternetConnection();
    }

    public ElementoObservable getInternetConnectionStatus(){ return internetConnectionStatus; }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void hasActiveInternetConnection() {
        if (isNetworkConnected()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.setReadTimeout(15000);
                urlc.connect();
                if(urlc.getResponseCode() == 200) {
                    internetConnectionStatus.setElemento(1);
                }
            } catch (java.net.SocketTimeoutException ste) {
                internetConnectionStatus.setElemento(-1);
                System.out.println("TIMEOUT: revisar conexión a internet");
            } catch (IOException e) {
                internetConnectionStatus.setElemento(-2);
                System.out.println("Error checking internet connection"+ e);
            }
        } else {
            internetConnectionStatus.setElemento(-1);
            System.out.println("Network connection: "+ "No network available!");
        }
    }
}
 */