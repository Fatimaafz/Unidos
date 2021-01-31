package com.example.unidos;
//changeq
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;
import com.example.unidos.repository.User;
import com.example.unidos.repository.UserRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class NewAccountViewModel extends ViewModel {
    /** A live data variable notify views
     * when a change occur. **/
    /** To get the date value. **/

    private ReportedPerson person = new ReportedPerson();
    public MutableLiveData<Integer> opResult = new MutableLiveData<>();


    public MutableLiveData<Boolean> isBtnPressed = new MutableLiveData<>();

    public MutableLiveData<String> date = new MutableLiveData<>();
    /** To tell the observer display the datepicker. **/
    public MutableLiveData<Boolean> btDate = new MutableLiveData<>();
    /** To tell the observer to display the options. **/
    public MutableLiveData<Boolean> btSex = new MutableLiveData<>();
    /** To enable or disable the button
     * create account (bt_ok).**/
    public MutableLiveData<Boolean> statusBtCreate = new MutableLiveData<>();
    /**  To notify the observer that
     * an error was detected in field date. **/
    public MutableLiveData<Boolean> dateError = new MutableLiveData<>();
    /** To get the name value. **/
    public MutableLiveData<String> name = new MutableLiveData<>();
    /** To get the secondName value. **/
    public MutableLiveData<String> secondName = new MutableLiveData<>();
    /** To get the surname value. **/
    public MutableLiveData<String> surname = new MutableLiveData<>();
    /** To get the surname2 value. **/
    public MutableLiveData<String> secondSurname = new MutableLiveData<>();
    /** To get the curp value. **/
    public MutableLiveData<String> curp = new MutableLiveData<>();
    /** To get the sex value. **/
    public MutableLiveData<String> sex = new MutableLiveData<>();
    /** To notify the observer that
     * an error was detected in field sex.*/
    public MutableLiveData<Boolean> sexError = new MutableLiveData<>();
    /** To notify the observer that
     * an error was detected in other fields. **/
    public MutableLiveData<String> errors = new MutableLiveData<>();
    /** To get the phone value. **/
    public MutableLiveData<String> phone = new MutableLiveData<>();
    /** To notify the observer about
     * the status of the request (create an account). **/
    public MutableLiveData<String> actionBtNewAcc = new MutableLiveData<>();

    Context context;
    List<Boolean> flags = new ArrayList<>();
    String selectedDate;
    String selectedSex;
    String nameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)+$";

    String secondNameRE="^([a-zA-Z\\á\\ó\\í\\é\\ú\\Á\\É\\Í\\Ó\\Ú\\ñ\\Ñ\\Ü\\ü\\â\\ê\\î\\ô\\û\\Â\\Ê\\Î\\Ô\\Û\\à\\è\\ù\\ë\\ï\\ö\\ü\\ÿ\\Ë\\Ï\\Ü\\ç]{2,20}[ ]?)*$";
    String phoneRE="[0-9]{10}";
    String curpRE= "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$";

    VerifyPhoneActivity verification = new VerifyPhoneActivity();

    public ReportedPerson getPerson() {
        return person;
    }

    /** When the user presses the date EditText */
    public void displayDatePicker(){
        date.setValue("");
        System.out.println("Debería desplegarse el DatePicker");
        /** The value of the observable variable
         * changes to true in order to
         * display the calendar **/
        btDate.setValue(true);
        /** To verify the format of the value
         * selected **/
        //checkDate();
    }

    /** Method to check the value selected
     * when the observer detect a change
     * in the observable variable date **/
    public void checkDate(){
        System.out.println("El valor de la fecha es: "+ date.getValue());
        /** System must not allow null values. */
        if(date.getValue().equals(null)){
            /** To tell observers that the value is wrong **/
            dateError.setValue(true);
            /** Set false to the value 5 of the list
             * Its purpose is to know if there are errors
             * in the fields and disable
             *  the button create account. **/
            flags.set(5, false);
        }else {
            /** System must not allow empty values. **/
            if (date.getValue().isEmpty()) {
                /** To tell observers the value is wrong. **/
                dateError.setValue(true);
                /** Set false to the value 5 of the list **/
                /** Its purpose is to know if there are
                 * errors in the fields and disable
                 * the button create account. **/
                flags.set(5, false);
            } else {
                /** To tell observers the value isn't empty. */
                dateError.setValue(false);
                /** Set true to the value 5 of the list **/
                /** Its purpose is to know if there are
                 * errors in the fields and disable
                 * the button create account. */
                flags.set(5, true);
            }
        }
        /** Method which check the state of every
         * field and then set an enable
         * or disable value to the button create account. */
        checkFlags();
    }

    /** When the user presses the
     * AutoCompleteTextView. **/
    public void displayMenu(){
        System.out.println("Debería desplegarse el Menú");
        /** The value of the observable variable
         * changes to true in order to
         * display the dropDownMenu **/
        btSex.setValue(true);
    }

    /** Method to check the value selected when
     * the observer detect a change
     * in the observable variable sex */
    public void checkSex(){
        if(sex.getValue().equals(null)){
            /** To tell observers that the value
             * is wrong. **/
            sexError.setValue(true);
            /** Set false to the value 6 of the list **/
            /** Its purpose is to know if there are
             * errors in the fields and disable
             * the button create account. */
            flags.set(6, false);
        }else {
            if (sex.getValue().isEmpty()) {
                /** To tell observers that
                 * the value is wrong. **/
                sexError.setValue(true);
                /** Set false to the value 6 of the list **/
                /** Its purpose is to know if there are
                 * errors in the fields and disable
                 * the button create account. **/
                flags.set(6, false);
            } else {
                /** To tell observers that the value is
                 * not null or empty. **/
                sexError.setValue(false);
                /** Set true to the value 6 of the list */
                /** Its purpose is to know if there are
                 * errors in the fields and disable
                 * the button create account. */
                flags.set(6, true);
            }
        }
        /** Method which check the state of every
         * field and then set an enable
         * or disable value to the button create account. **/
        checkFlags();
    }

    /** Method to check the typed value by the user
     * when the observer detect a change
     * in the observable variable (name, secondname,
     * surname, secondSurname, curp and phone) */
    public void checkField(int id){
        /** To know the field where the user is typing. **/
        switch (id) {
            /** The user is typing in the CURP field. **/
            case 0:
                System.out.println("El valor del campo es: "+ curp.getValue());
                /** Is the field empty? **/
                if (curp.getValue() == null)
                    errors.setValue("emptyCurp");
                else
                if (curp.getValue().isEmpty()){
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field CURP */
                    errors.setValue("emptyCurp");
                    /** Set false to the value 0 of the list **/
                    /** Its purpose is to know if there are
                     * errors in the fields and disable
                     * the button create account. */
                    flags.set(0,false);
                }else{
                    if(curp.getValue().matches(curpRE)){
                        /** The value of the observable variable
                         * errors changes in order to
                         * hide the error section under the CURP */
                        errors.setValue("fillCurp");
                        /** Set true to the value 0 of the list **/
                        /** Its purpose is to know if there are
                         * errors in the fields and disable
                         * the button create account. */
                        flags.set(0, true);
                    }else {
                        System.out.println("entré");
                        /** The value of the observable variable
                         * errors changes in order to
                         * display an error under the field CURP */
                        errors.setValue("wrongSyntaxCurp");
                        /** Set false to the value 0 of the list
                         /** Its purpose is to know if there are
                         errors in the fields and disable
                         the button create account. **/
                        flags.set(0, false);
                    }
                }
                break;
            case 1: /** The user is typing in the name field. **/
                /** Is the field empty? **/
                if (name.getValue().isEmpty()) {
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field name */
                    errors.setValue("emptyName");
                    /** Set false to the value 1 of the list **/
                    /** Its purpose is to know if there
                     * are errors in the fields and disable
                     * the button create account. */
                    flags.set(1,false);
                } else{
                    if(name.getValue().matches(nameRE)){
                        /** The value of the observable variable
                         * errors changes in order to
                         * hide the error section under the field */
                        errors.setValue("fillName");
                        /** Set true to the value 1 of the list **/
                        /** Its purpose is to know if there are
                         * errors in the fields and disable
                         * the button create account. */
                        flags.set(1, true);
                    }else{
                        /** The value of the observable variable
                         * errors changes in order to
                         * display an error under the field name */
                        errors.setValue("wrongSyntaxName");
                        /** Set false to the value 1 of the list
                         /** Its purpose is to know if there
                         are errors in the fields and disable
                         the button create account. **/
                        flags.set(1, false);
                    }
                }
                break;
            case 2: /** The user is typing in the
             secondName field. **/
                /** Does the value matches? */
                if(secondName.getValue().matches(secondNameRE)){
                    /** The value of the observable variable
                     * errors changes in order to
                     * hide the error section under the field */
                    errors.setValue("fillName2");
                    /** Set false to the value 2 of the list **/
                    /** Its purpose is to know if there are
                     * errors in the fields and disable
                     * the button create account. */
                    flags.set(2, true);
                }else{
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field name */
                    errors.setValue("wrongSyntaxName2");
                    /** Set false to the value 2 of the list
                     /* Its purpose is to know if there are
                     errors in the fields and disable
                     the button create account. */
                    flags.set(2, false);
                }
                break;
            case 3: /** The user is typing in the surname field. */
                /** Is the field empty? */
                if (surname.getValue().isEmpty()) {
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field surname */
                    errors.setValue("emptySurname");
                    /** Set false to the value 3 of the list */
                    /** Its purpose is to know if there are
                     * errors in the fields and disable
                     * the button create account. */
                    flags.set(3,false);
                } else{
                    if(surname.getValue().matches(nameRE)){
                        /** The value of the observable variable
                         * errors changes in order to
                         * hide the error section under the field */
                        errors.setValue("fillSurname");
                        /** Set true to the value 3 of the list
                         /** Its purpose is to know if there are
                         errors in the fields and disable
                         the button create account. **/
                        flags.set(3, true);
                    }else{
                        /** The value of the observable variable
                         * errors changes in order to
                         * display an error under the field surname */
                        errors.setValue("wrongSyntaxSurname");
                        /** Set false to the value 3 of the list
                         /** Its purpose is to know if there are
                         errors in the fields and disable
                         the button create account. **/
                        flags.set(3, false);
                    }
                }
                break;
            case 4: /** The user is typing in
             the secondSurname field **/
                System.out.println("El valor del campo es: "+ secondSurname.getValue());
                /** Does the value match? **/
                if(secondSurname.getValue().matches(secondNameRE)){
                    /** The value of the observable variable
                     * errors changes in order to
                     * hide the error section under the field */
                    errors.setValue("fillSurname2");
                    /** Set true to the value 4 of the list
                     /* Its purpose is to know if there are
                     errors in the fields and disable
                     the button create account. */
                    flags.set(4, true);
                }else{
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field secondSurname */
                    errors.setValue("wrongSyntaxSurname2");
                    /** Set false to the value 3 of the list
                     /* Its purpose is to know if there are
                     errors in the fields and disable
                     the button create account. */
                    flags.set(4, false);
                    System.out.println("Sexo seleccionado: "+ selectedSex);
                }
                break;
            case 7: /** The user is typing in the phone field. **/
                System.out.println("El valor del campo es: "+ phone.getValue());
                if (phone.getValue().isEmpty()) {
                    /** The value of the observable variable
                     * errors changes in order to
                     * display an error under the field phone */
                    errors.setValue("emptyPhone");
                    /** Set false to the value 7 of the list
                     /* Its purpose is to know if there are
                     errors in the fields and disable
                     the button create account. */
                    flags.set(7, false);
                } else{
                    /** Does the value match? **/
                    if(phone.getValue().matches(phoneRE)){
                        /** The value of the observable variable
                         * errors changes in order to
                         * hide the error section under the field */
                        errors.setValue("fillPhone");
                        /** Set true to the value 7 of the list
                         /* Its purpose is to know if there are
                         errors in the fields and disable
                         the button create account. */
                        flags.set(7, true);
                    }else{
                        /** The value of the observable variable
                         * errors changes in order to
                         * display an error under the field surname */
                        errors.setValue("wrongSyntaxPhone");
                        /** Set false to the value 7 of the list
                         /* Its purpose is to know if there are
                         errors in the fields and disable
                         the button create account. */
                        flags.set(7, false);
                    }
                }
                break;
        }
        /** Method which check the state of every
         * field and then set an enable
         * or disable value to the button create account. */
        checkFlags();
    }

    /** Method which check the state of every field
     * and then set an enable
     * or disable value to the button create account. */
    private void checkFlags(){
        int i=0;
        boolean status=false;
        while(i<flags.size()){
            if(!flags.get(i)) {
                status=false;
                i=flags.size()+1;
            }else status=true;
            i++;
        }
        System.out.println("ESTADO DEL BOTÓN: "+ status);
        /** Update the observable variable */
        statusBtCreate.setValue(status);
    }

    /**Method called by New Account class
     /* It set 5 positions to false in order to keep the button disable
     on app start. */
    public void fill(){
        for(int i=0; i<8; i++){
            flags.add(false);
        }
        flags.set(2,true);
        flags.set(4,true);
        for(Boolean b : flags)
            System.out.println("ESTADOS X CAMPO: "+ b);
    }

    /** Execute its content every time
     * the user presses it. **/
    public void onRegisterClick(){
        isBtnPressed.setValue(true);
    }

    public void register(){
        final String phoneNumber=phone.getValue();
        /** User instance because the insert method in UserRepository
         * requires an User object. */
        final User user = new User(getCURP(), getName(), getSecondName(), getSurname(), getSecondSurname(), date.getValue(), sex.getValue(), getPhone());
        /** Instance of UserRepository class which will connect with firebase
         * and its principal job is request operations (read, write and delete data)
         * to Firebase. */
        final UserRepository userRepository = new UserRepository();
        /** The variable checkCurpExistenceObservable of UserRepository class
         * will be observed. */
        userRepository.getCurpExistence().addObserver(new Observer() {
            /** When the observable variable changes state. **/
            @Override
            public void update(Observable o, Object arg) {
                /** Actual value of the observable. **/
                ElementoObservable<Integer> elementoABC = (ElementoObservable) o;
                /** Cast and assign the observable
                 * value to an integer variable
                 * in order to determine the action to execute. */
                final int elementCURP= Integer.parseInt(elementoABC.getElemento().toString());
                /** If the new value of the observable is -1,
                 * it means the CURP is already registered and
                 * the system doesn't allow duplicated values.
                 */
                if(elementCURP==-1) {
                    /**  Value to display a message error. **/
                    actionBtNewAcc.setValue("hideProgressBar:existingCURP");
                }else if(elementCURP == 1){
                    /** If the new value of the observable is 1,
                     * it means the CURP is not registered and
                     * the system can register it. */
                    System.out.println("no se encuentra registrada la curp");
                    Log.i("View model: ", "La CURP ingresada no se encuentra registrada");
                    /** Delete the obsever, we don't want to
                     * observe the changes of the
                     * checkCurpExistenceObservable variable. */
                    userRepository.getCurpExistence().deleteObservers();
                    /** The variable checkPhoneExistenceObservable
                     * of UserRepository class
                     * will be observed. */
                    userRepository.getPhoneExistence().addObserver(new Observer() {
                        /** When the observable variable changes state. **/
                        @Override
                        public void update(Observable o, Object arg) {
                            /** Cast and assign the observable value to an
                             * integer variablein order to determine
                             * the action to execute. */
                            int elementPhone = Integer.parseInt(((ElementoObservable) o).getElemento().toString());
                            System.out.println("ESTOY AÑADIENDO EL OBSERVER");
                            System.out.println("---> en View Model phone: "+ elementPhone);
                            if(elementPhone == -1) {
                                /** If the new value of the observable is -1,
                                 * it means the phone is already registered
                                 * and the system doesn't allow duplicated values.
                                 */
                                /** Value to display a message error. **/
                                actionBtNewAcc.setValue("hideProgressBar:existingPhone");
                            }else if(elementPhone == 1){
                                /** If the new value of the observable is 1,
                                 * it means the CURP is not registered and
                                 * the system can register it.
                                 */
                                /** Delete the observer, we don't
                                 *  want to observe the changes of the
                                 *  checkPhoneExistenceObservable variable. */
                                userRepository.getPhoneExistence().deleteObservers();
                                /** The variable verificationStatusObservable
                                 * of VerifyPhoneActivity class
                                 * will be observed. */
                                verification.getVerificationStatusObservable().addObserver(new Observer() {
                                    @Override
                                    public void update(Observable o, Object arg) {
                                        /** Cast and assign the
                                         * observable value to an integer variable
                                         * in order to determine
                                         * the action to execute. */
                                        int element = Integer.parseInt(((ElementoObservable) o).getElemento().toString());
                                        System.out.println("---> en View Model: "+ element);
                                        /** If the new value of the
                                         * observable is 1, it means
                                         * the selfCheck was successful
                                         */
                                        if (element == 1) {
                                            System.out.println("Entré al if");
                                            /** Delete the observer,
                                             * we don't want to observe the changes of the
                                             * verificationStatusObservable variable. */
                                            verification.getVerificationStatusObservable().deleteObservers();
                                            /** The variable insertStatusObservable
                                             * of VerifyPhoneActivity class
                                             * will be observed. */
                                            userRepository.getInsertStatus().addObserver(new Observer() {
                                                @Override
                                                public void update(Observable o, Object arg) {
                                                    /** Successful insertion **/
                                                    if(Boolean.parseBoolean(((ElementoObservable) o).getElemento().toString())) {
                                                        /** Update the value in order
                                                         * to indicate that the info were
                                                         * stored and allow the access. */
                                                        actionBtNewAcc.setValue("hideProgressBar:successInserting");
                                                    }else{ /** The insertion fail **/
                                                        Log.i("View model: ", "No se realizó el Registro");
                                                        /** Value to display a message error. **/
                                                        actionBtNewAcc.setValue("hideProgressBar:failInserting");
                                                    }
                                                }
                                            });
                                            userRepository.insert(user);
                                        }else {
                                            /** The selfCheck goes wrong. **/
                                            actionBtNewAcc.setValue("hideProgressBar:fail");
                                        }
                                    }
                                });
                                /** The system must observe if the time for the self verification expired. **/
                                verification.getRetrievalTimeObservable().addObserver(new Observer() {
                                    @Override
                                    public void update(Observable o, Object arg) {
                                        /**If expired **/
                                        if (Boolean.parseBoolean(((ElementoObservable) o).getElemento().toString()))
                                        /** To show an error message in the UI. **/
                                            actionBtNewAcc.setValue("hideProgressBar:fail");
                                    }
                                });
                                /** Method call to send the
                                 * message to the phone and
                                 * start the self verification. **/
                                verification.sendVerificationMethod(phoneNumber);
                            }else if(elementPhone == -2) {
                                /** An unknown error happened. **/
                                userRepository.getPhoneExistence().deleteObservers();
                                actionBtNewAcc.setValue("hideProgressBar:operationFail");
                            }
                        }
                    });
                    /** Method call to search if the value
                     * of the phone is already registered. **/
                    userRepository.readPhone(getPhone());
                }else if(elementCURP == -2) {
                    /** An unknown error happened. **/
                    userRepository.getCurpExistence().deleteObservers();
                    actionBtNewAcc.setValue("hideProgressBar:operationFail");
                }
            }
        });
        /** Method call to search if the value
         * of the CURP is already registered. **/
        userRepository.readCURP(getCURP());
    }

    public void setContext(Context context){
        this.context=context;
    }

    /** The context is needed to perform some
     * actions like check the Internet connection
     * and to use some methods of PersistentData */
    private Context getContext(){
        return context;
    }

    public NewAccountViewModel(){

    }

    /** return the name value without
     * spaces at the beginning and the end. **/
    private String getName(){
        return name.getValue().trim();
    }

    /** return the second name value
     * without spaces at the beginning and the end. **/
    private  String getSecondName(){
        String name2 = secondName.getValue();
        if (!(name2 == null)) {
            if(name2.equals("")) name2 = null;
            else name2.trim();
        }
        System.out.println("SECOND NAME: "+name2);
        return name2;
    }

    /** return the surname value
     * without spaces at the beginning and the end. **/
    private String getSurname(){
        return surname.getValue().trim();
    }

    /** return the surname2 value
     * without spaces at the beginning and the end. **/
    private String getSecondSurname(){
        String surname2 = secondSurname.getValue();
        if (!(surname2 == null)) {
            if (surname2.equals("")) surname2 = null;
            else surname2.trim();
        }
        System.out.println("SURNAME2: "+surname2);
        return surname2;
    }

    /** return the CURP value
     * without spaces at the beginning and the end. **/
    public String getCURP(){
        return curp.getValue().toUpperCase().trim();
    }

    /** return the phone value
     * without spaces at the beginning and the end. **/
    private String getPhone(){
        System.out.println("TELÉFONO: "+ phone.getValue());
        return phone.getValue().trim();
    }

    public void setSex(String selectedSex){
        this.selectedSex=selectedSex;
        sex.setValue(selectedSex);
    }

    /** To get the selected date. **/
    public void setDate(String selectedDate){
        this.selectedDate=selectedDate;
        date.setValue(selectedDate);
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
                            opResult.setValue(1);
                            break;
                        case -1:
                            opResult.setValue(-1);
                            break;
                        case -2:
                            opResult.setValue(-2);
                            break;
                    }
                }
            });
        }else
            opResult.setValue(-3);
        personRepository.getOnePerson( curp);
    }

}