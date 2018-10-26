package com.sasam.virtuallibrary.JoinGroup;

import android.app.Activity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tryForReJoin implements Join {
    private Activity activity;
    tryForReJoin(Activity activity){
        this.activity = activity;
    }
    @Override
    public void grpJoin() {
        if(!activity.isFinishing())
        {
            showAlertError();
        }
    }


    private void showAlertError(){
        new SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("You are already in this Group!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })
                .show();
    }
}
