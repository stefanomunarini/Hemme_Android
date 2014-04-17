package com.povodev.hemme.android.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Stefano on 17/04/14.
 */
public class Formatters {

    public static String timeFormat(int time) {
        return time/1000+"";
    }

    public static String timestampFormat(Timestamp date) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
    }

    public static String dateFormat(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }
}
