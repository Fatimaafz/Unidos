package com.example.unidos.shared;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class KeyBoard extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view){
        return source;
    }
}
