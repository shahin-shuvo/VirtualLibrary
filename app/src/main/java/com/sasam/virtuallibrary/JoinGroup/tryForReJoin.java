package com.sasam.virtuallibrary.JoinGroup;

import android.app.Activity;
import android.widget.EditText;

import com.sasam.virtuallibrary.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tryForReJoin implements Join {
    public EditText joinCode;
    private Activity activity;
    tryForReJoin(Activity activity){
        this.activity = activity;
    }
    @Override
    public void grpJoin() {
        joinCode = (EditText) activity.findViewById(R.id.inputCode);
        if(!activity.isFinishing())
        {
            showAlertError();
            joinCode.setText(null);
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
