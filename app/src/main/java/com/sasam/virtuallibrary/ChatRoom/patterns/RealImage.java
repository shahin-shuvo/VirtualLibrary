package com.sasam.virtuallibrary.ChatRoom.patterns;
//proxy pattern
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class RealImage implements ImageSelector{

    private String avataStr;
    public RealImage(String avataStr){
        this.avataStr=avataStr;
    }

    @Override
    public Bitmap setImage() {
        byte[] decodedString = Base64.decode(avataStr, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
