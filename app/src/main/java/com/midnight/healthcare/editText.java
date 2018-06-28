package com.midnight.healthcare;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by TUSK.ONE on 6/26/16.
 */
public  class editText extends EditText {

    public editText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets() , "Lato-Regular.ttf"));
    }
}
