package com.sasam.virtuallibrary.ChatRoom.patterns;
//adapter pattern
import android.graphics.Bitmap;

import com.sasam.virtuallibrary.ChatRoom.util.ImageUtils;

import java.io.InputStream;

public class BitmapToStringAdapter extends StringBase64{

    private Bitmap bitmap;

    public BitmapToStringAdapter(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    @Override
    public String getStringBase64() {
        bitmap = ImageUtils.cropToSquare(bitmap);
        InputStream is = ImageUtils.convertBitmapToInputStream(bitmap);
        final Bitmap liteImage = ImageUtils.makeImageLite(is,
                bitmap.getWidth(),bitmap.getHeight(),
                ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT);

        return ImageUtils.encodeBase64(liteImage);
    }
}
