package com.midnight.healthcare;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by TUSK.ONE on 6/26/16.
 */
public  class BtnHelvetica extends Button {

    public BtnHelvetica(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets() , "HelveticaNeue-Regular.ttf"));
    }
}
