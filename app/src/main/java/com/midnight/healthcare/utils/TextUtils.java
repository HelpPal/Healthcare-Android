package com.midnight.healthcare.utils;

import android.widget.EditText;

import java.util.Random;


public class TextUtils {
    public static String editTextValidation(EditText et) {
        return et.getText() == null ? "" : et.getText().toString();
    }

    public static float rand() {
        Random rand = new Random();
        float rating = rand.nextFloat() * (5f - 0f) + 0f;
        return round(rating, 1);
    }
    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }
}
