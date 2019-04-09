package com.aaronzadev.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateManager {

    private static SimpleDateFormat sdf;
    private static final String PATTERN_DATE = "dd/MM/yyyy";

    public static String getcurrentDate(){

        sdf = new SimpleDateFormat(PATTERN_DATE);
        return sdf.format(new Date());

    }

}
