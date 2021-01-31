package com.example.unidos.shared;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DialogViewModel extends ViewModel {
    public MutableLiveData<String> dialogText;
    public MutableLiveData<String> titleText;
    private MutableLiveData<Boolean> dismiss;

    public DialogViewModel(){
        dialogText = new MutableLiveData<>();
        titleText = new MutableLiveData<>();
        dismiss = new MutableLiveData<>();
    }

    public void setDialogText(String dialogText) {
        this.dialogText.setValue(dialogText);
    }

    public MutableLiveData<Boolean> getDismiss() {
        return dismiss;
    }

    public void dismissDialog(){
        dismiss.setValue(true);
    }

    public void doAction(){
        dismiss.setValue(false);
    }

}
