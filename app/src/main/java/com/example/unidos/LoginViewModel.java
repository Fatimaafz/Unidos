package com.example.unidos;
//changeq
import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.Observable;
import java.util.Observer;

public class LoginViewModel extends ViewModel {
    /** A live data variable notify views when a change occur.**/
    /** To enable or disable the login button. **/
    public MutableLiveData<Boolean> btLogEnable = new MutableLiveData<>();
    /** To get the CURP value. **/
    public MutableLiveData<String> logCURP = new MutableLiveData<>();
    /** To notify the observer that an error was detected. **/
    public final MutableLiveData<String> logCURPError = new MutableLiveData<>();
    /** To notify the observer what happened during the CURP search. **/
    public final MutableLiveData<String> btLogin = new MutableLiveData<>();
    /** To notify the observer that the user has clicked the button Create Account. **/
    public final MutableLiveData<Boolean> btNewAcc = new MutableLiveData<>();

    Context context;
    String reCURP= "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";


    /** While the user is typing on the field CURP, the observer
     * is observing the changes and
     * telling this ViewModel to validate the typed value. */
    public boolean validData(){
        /** To disable the button login **/
        btLogEnable.setValue(false);
        /** In case the CURP value was null **/
        if(logCURP.getValue() == null){
            btLogEnable.setValue(false);
            /** Set a value to logCURPError so that
             * the observer can detect
             * the change and then execute an action. **/
            logCURPError.setValue("emptyField");
        }else if (logCURP.getValue().isEmpty()) {
            /** Is the field empty?**/
                System.out.println("Está vacío");
                btLogEnable.setValue(false);
                logCURPError.setValue("emptyField");
            } else {
            /** Does the CURP match the regular expression? **/
                if (logCURP.getValue().matches(reCURP)) {
                    System.out.println("coincidio conla RE: "+ logCURP.getValue());
                    logCURPError.setValue("match");
                    /** Enable the login button.**/
                    btLogEnable.setValue(true);
                } else { /** The value doesn't match
                 the regular expression. **/
                    /** Disable the login button.**/
                    btLogEnable.setValue(false);
                    System.out.println("error de sintaxis: "+ logCURP.getValue());
                    logCURPError.setValue("wrongSyntax");
                }
            }
        return btLogEnable.getValue();
    }

    /** When the button login is enable and the user presses it. **/
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

    /** Method which will search the CURP in the database. **/
    public void checkCURP(){
        /** Instance of UserRepository class which will connect
         * with firebase and its principal job is request
         * operations (read, write and delete data)
          to Firebase. */
        final UserRepository userRepository = new UserRepository();
        /** The variable checkCurpExistenceObservable
         * of UserRepository class will be observed. */
        userRepository.getCurpExistence().addObserver(new Observer() {
            /** When the observable variable changes state. **/
            @Override
            public void update(Observable o, Object arg) {
                /** Cast and assign the observable value to an integer variable
                 * in order to determine the action to execute. */
                final int elementCURP= Integer.parseInt(((ElementoObservable) o).getElemento().toString());
                System.out.println("ESTOY AÑADIENDO EL OBSERVER");
                System.out.println("---> en View Model curp: "+ elementCURP);
                /** If the new value of the observable is -1,
                 * it means the CURP is already registered and
                 * now the user is allowed to access. */
                if(elementCURP==-1) {
                    /** Inform the observer the user must access to the app. */
                    btLogin.setValue("success");
                }else if(elementCURP == 1) {
                    /** If the new value of the observable is 1, it means
                     * the CURP is not registered and the user
                     * is not allowed to access. */
                    userRepository.getCurpExistence().deleteObservers();
                    /** Inform the observer the user mustn't
                     * access to the app. **/
                    btLogin.setValue("userNotFound");
                }else if(elementCURP == -2) {
                    /** An error occur during the searching **/
                    userRepository.getCurpExistence().deleteObservers();
                    /**  Inform the observer the error. **/
                    btLogin.setValue("operationFailed");
                }
            }
        });
        userRepository.readCURP(getCURP());
    }

    /** Method which goal is save the CURP into
     * file containing key-value. **/
    public void savePersistentData(Context context){
        /** Instance of PersistentData class
         * which will store the key-value. **/
        PersistentData persistentData = new PersistentData(context);
        /** Send the CURP to the class. **/
        persistentData.setValues(getCURP());
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
