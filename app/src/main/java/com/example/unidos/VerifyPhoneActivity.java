package com.example.unidos;
//changeq
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity {
    private String verificationId;
    private FirebaseAuth mAuth;
    int verificationStatus=0;
    private ElementoObservable<Integer> verificationStatusObservable;   // Se agrega el observable
    private ElementoObservable<Boolean> retrievalTimeObservable; //DELETE
    private ElementoObservable<Boolean> phoneVerifTimeOutObservable;
    boolean retrievalTime;

    public VerifyPhoneActivity(){
        mAuth = FirebaseAuth.getInstance();
        verificationStatusObservable = new ElementoObservable<>();
        verificationStatusObservable.adherirElemento(verificationStatus);
        retrievalTimeObservable = new ElementoObservable<>(); // DELETE
        retrievalTimeObservable.adherirElemento(retrievalTime); //DELETE
        phoneVerifTimeOutObservable = new ElementoObservable<>();
        //sendVerificationCode(phoneNumber);
    }

    public void sendVerificationMethod(String phoneNumber){
        System.out.println("TELÉFONO EN VERIFY: "+ phoneNumber);
        sendVerificationCode("+52"+phoneNumber); }


    /**
     *  Este get va a devolver el observable
     * */
    public ElementoObservable getPhoneVerifTimeOut(){
        return phoneVerifTimeOutObservable;
    }

    public ElementoObservable getVerificationStatusObservable() {
        return verificationStatusObservable;
    }

    public ElementoObservable getRetrievalTimeObservable(){ //DELETE
        return retrievalTimeObservable;
    }


    public int  getVerificationStatus(){
        return verificationStatus;
    }

    public void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    System.out.println("OPERACIÓN EXITOSA");
                }else{
                    //verificationStatus=false;
                    System.out.println("HA OCURRIDO UN ERROR");
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String s){
            retrievalTime = true; //DELETE
            retrievalTimeObservable.setElemento(true); //DELETE
            phoneVerifTimeOutObservable.setElemento(true);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                System.out.println("AQUI ESTOY");
                verificationStatusObservable.setElemento(1);     // Se setea el valor de la propiedad
                verificationStatus=1;
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            System.out.println("falló la verificación");
            verificationStatusObservable.setElemento(2);    // Se setea el valor de la propiedad
            verificationStatus=2;
            Log.i("@@@@ Exception", e.toString());
        }
    };
}