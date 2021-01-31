package com.example.unidos;
//changeq
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.example.unidos.repository.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.Observable;
import java.util.Observer;

public class LoginViewModel extends ViewModel {
    /** A live data variable notify views when a change occur.**/
    /**
     * To enable or disable the login button.
     **/
    public MutableLiveData<Boolean> btLogEnable = new MutableLiveData<>();
    /**
     * To get the CURP value.
     **/
    public MutableLiveData<String> logCURP = new MutableLiveData<>();
    /**
     * To notify the observer that an error was detected.
     **/
    public final MutableLiveData<String> logCURPError = new MutableLiveData<>();
    /**
     * To notify the observer what happened during the CURP search.
     **/
    public final MutableLiveData<String> btLogin = new MutableLiveData<>();
    /**
     * To notify the observer that the user has clicked the button Create Account.
     **/
    public final MutableLiveData<Boolean> btNewAcc = new MutableLiveData<>();

    private MutableLiveData<Integer> opResPerson = new MutableLiveData<>();
    private ReportedPerson person = new ReportedPerson();

    Context context;
    String reCURP = "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";

    public MutableLiveData<Integer> getOpResPerson() {
        return opResPerson;
    }

    public ReportedPerson getPerson() {
        return person;
    }

    /**
     * While the user is typing on the field CURP, the observer
     * is observing the changes and
     * telling this ViewModel to validate the typed value.
     */
    public boolean validData() {
        /** To disable the button login **/
        btLogEnable.setValue(false);
        /** In case the CURP value was null **/
        if (logCURP.getValue() == null) {
            btLogEnable.setValue(false);
            /** Set a value to logCURPError so that
             * the observer can detect
             * the change and then execute an action. **/
            logCURPError.setValue("emptyField");
        } else if (logCURP.getValue().isEmpty()) {
            /** Is the field empty?**/
            System.out.println("Está vacío");
            btLogEnable.setValue(false);
            logCURPError.setValue("emptyField");
        } else {
            /** Does the CURP match the regular expression? **/
            if (logCURP.getValue().matches(reCURP)) {
                System.out.println("coincidio conla RE: " + logCURP.getValue());
                logCURPError.setValue("match");
                /** Enable the login button.**/
                btLogEnable.setValue(true);
            } else { /** The value doesn't match
             the regular expression. **/
                /** Disable the login button.**/
                btLogEnable.setValue(false);
                System.out.println("error de sintaxis: " + logCURP.getValue());
                logCURPError.setValue("wrongSyntax");
            }
        }
        return btLogEnable.getValue();
    }

    /**
     * When the button login is enable and the user presses it.
     **/
    public void log() {
        /** Change the value so that the observer
         * of the variable show the progress bar. **/
        btLogin.setValue("showProgressBar");
        /** Instance of the Connection class which will verify whether
         * the phone has Internet connection. */
        Connection connection = new Connection(context);
        /** Is the phone connected to a network? **/
        if (connection.isNotConnected()) {
            /** The observer will display a message **/
            btLogin.setValue("noConnected");
        } else if (connection.checkConnection()) {
            /** In case the mobile has an stable
             * Internet connection. */
            System.out.println("Sí cuenta con la calidad deseada");
            checkCURP(); /** Execute the method. **/
        } else { /** Probably the connection is unstable. **/
            /** The observer will detect the change in the value and
             * then it will show an error message. */
            btLogin.setValue("hasntInternet");
            System.out.println("No cuenta con la calidad deseada");
        }
    }

    /**
     * Method which will search the CURP in the database.
     **/
    public void checkCURP() {

        final UserRepository userRepository = new UserRepository();

        userRepository.getCurpExistence().addObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {

                final int elementCURP = Integer.parseInt(((ElementoObservable) o).getElemento().toString());
                System.out.println("ESTOY AÑADIENDO EL OBSERVER");
                System.out.println("---> en View Model curp: " + elementCURP);

                if (elementCURP == -1) {

                    btLogin.setValue("success");
                } else if (elementCURP == 1) {

                    userRepository.getCurpExistence().deleteObservers();

                    btLogin.setValue("userNotFound");
                } else if (elementCURP == -2) {

                    userRepository.getCurpExistence().deleteObservers();

                    btLogin.setValue("operationFailed");
                }
            }
        });
        userRepository.readCURP(getCURP());
    }

    public void getIfPersonNear(String curp, LatLng personLoc, LatLng userLocation) {
        Log.i("^^^{ ", "lets get the info");
        final ReportedPersonRepository personRepository = new ReportedPersonRepository();
        if(personRepository.isPersonNear(personLoc, userLocation)) {
            personRepository.getOpResult().addObserver(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    int res = (int) ((ElementoObservable) o).getElemento();
                    switch (res) {
                        case 1:
                            person = personRepository.getPerson();
                            opResPerson.setValue(1);
                            break;
                        case -1:
                            opResPerson.setValue(-1);
                            break;
                        case -2:
                            opResPerson.setValue(-2);
                            break;
                    }
                }
            });
        }else
            opResPerson.setValue(-3);
        personRepository.getOnePerson( curp);
    }

    public String getCURP(){
        /** return the CURP value without spaces
         * at the begining and the end. **/
        return logCURP.getValue().toUpperCase().trim();
    }

    /** The context is needed to perform some
     * actions like check the Internet connection
     * and to use some methods of PersistentData */
    public void setContext(Context context){
        this.context=context;
    }

    /** When the user press the button create account. **/
    public void goToNewAcc(){
        btNewAcc.setValue(true);
    }

    /** Constructor **/
    public LoginViewModel(){
    }
}
