package com.sasam.virtuallibrary.ChatRoom.patterns;
//proxy pattern
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sasam.virtuallibrary.ChatRoom.data.StaticConfig;
import com.sasam.virtuallibrary.R;

public class ProxyImage implements ImageSelector{
    private RealImage realImage;
    private Context context;
    public ProxyImage(String avataStr,Context context){
        this.context=context;

        if(!avataStr.equals(StaticConfig.STR_DEFAULT_BASE64)){
            realImage=new RealImage(avataStr);
        }
    }

    @Override
    public Bitmap setImage() {
        if(realImage==null){
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata);
        }
        return realImage.setImage();
    }
}
