package com.pietrantuono.pietrantuonoevaluationtask.network;

import java.util.Locale;

/**
 * Created by Maurizio Pietrantuono, maurizio.pietrantuono@gmail.com.
 */
public class URLs {
    public static final String MAIN_URL="http://dynamic.pulselive.com/test/native/contentList.json";

    public static final String ITEM_URL_PREFIX="http://dynamic.pulselive.com/test/native/content/";
    public static final String ITEM_URL_SUFFIX=".json";


    public static String getItemURL(long itemId){
        return ITEM_URL_PREFIX.concat(String.valueOf(itemId)).concat(ITEM_URL_SUFFIX);
    }
}
