package com.midnight.healthcare;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by TUSK.ONE on 6/26/16.
 */
public  class TextHelvetica extends TextView {

    public TextHelvetica(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets() , "HelveticaNeue-Regular.ttf"));
    }
}
