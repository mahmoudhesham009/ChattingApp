package com.example.chattingapp;

import android.app.Activity;
import android.app.AlertDialog;

public class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;

    public LoadingDialog(Activity a){
        this.activity=a;
    }

    public void startDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setView(activity.getLayoutInflater().inflate(R.layout.loading_dialog,null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog(){
        alertDialog.dismiss();
    }

}
