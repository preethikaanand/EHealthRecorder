package com.eHealth.recorder.config;

import java.util.HashMap;

/**
 * Created by electrorobo on 5/2/16.
 */
public class MonthData_Info {
    private static HashMap<String, String> monthData;
    public static final String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static HashMap<String, String> getMonthData() {
        monthData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            if (i <= 9)
                monthData.put("0" + i, month[i-1]);
            else
                monthData.put("" + i, month[i-1]);
        }
        return monthData;
    }

    public static String getKeyFromValue(HashMap<String, String> stringHashMap, String value) {
        for (String s : stringHashMap.keySet()) {
            if (stringHashMap.get(s).equals(value)) {
                return s;
            }
        }
        return null;
    }
}
