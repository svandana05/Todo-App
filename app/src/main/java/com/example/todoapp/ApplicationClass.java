package com.example.todoapp;

import android.app.Activity;
import android.app.Application;

public class ApplicationClass extends Application {

    public static void backPressIntentAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_from_left,
                R.anim.slide_to_right);
    }

    public static void enterIntentAnim(Activity activity){
        activity.overridePendingTransition(R.anim.slide_from_right,
                R.anim.slide_to_left);
    }



}
