package com.pietrantuono.pietrantuonoevaluationtask.utils;

import android.os.Build;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class Utils {

    public static boolean weAreLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
