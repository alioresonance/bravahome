/*
 * *********************************************************
 *  Copyright (c) 2017 Brava Home Inc.  All rights reserved.
 * *********************************************************
 */
package bravahome.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by gzoldi on 02/09/2017 08:43 AM
 */
public class DateUtils {

    final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public String getCurrentTimestamp() {
        return new SimpleDateFormat(PATTERN).
                format(
                        new Timestamp( Calendar.getInstance().getTime().getTime() )
                );
    }

}