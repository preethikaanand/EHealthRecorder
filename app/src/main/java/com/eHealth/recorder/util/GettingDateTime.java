package com.eHealth.recorder.util;

import com.eHealth.recorder.dto.GraphData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GettingDateTime {
    private static GettingDateTime gettingDateTime;

    private GettingDateTime() {
    }

    public static GettingDateTime getInstance() {
        if (gettingDateTime == null) {
            gettingDateTime = new GettingDateTime();
        }
        return gettingDateTime;
    }

    public String gettingRegularExpression(GraphData criteriaForRegex) {
        String finalRegEx = "";

        switch (criteriaForRegex.getGraphDuration()) {
            case "Daily":
                finalRegEx = "(" + criteriaForRegex.getGraphDay() + ")/(" + criteriaForRegex.getGraphMonth() + ")-(" + criteriaForRegex.getGraphYear() + ")";
                break;
            case "Monthly":
                finalRegEx = "(0[1-9]|[1-9]|[12][0-9]|3[01])/(" + criteriaForRegex.getGraphMonth() + ")/(" + criteriaForRegex.getGraphYear() + ")";
                break;
            case "Yearly":
                finalRegEx = "(0[1-9]|[1-9]|[12][0-9]|3[01])/(0[4-9]|1[012]|[4-9])/(" + criteriaForRegex.getGraphYear() + ")";
                break;
        }

        return finalRegEx;

    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String dateFormatter(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public String dateComparator(String date) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = formatter.parse(getDate());
        Date serverDate = formatter.parse(date);
        int dateComparisonResult = serverDate.compareTo(currentDate);

        switch (dateComparisonResult) {
            case -1:
                return "-1"; //Date is before the Date argument
            case 0:
                return "0"; //Date is equal to the Date argument
            case 1:
                return "1"; //Date is after the Date argument

            default:
                return "";
        }
    }

    public long getInterval(long intervalMinutes) {
        int seconds = 60;
        int milliseconds = 1000;
        // For 00:00/12:00AM-->(24*60) minutes = 1440 minutes
        // (from startPoint 00:00:00 )
        long repeatMS = seconds * intervalMinutes * milliseconds;
        return repeatMS;
    }

    public Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public long getMorningStartOfAlarm(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public long getNoonStartOfAlarm(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public long getEveStartOfAlarm(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public String getPreviousDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return sdf.format(c.getTime());
    }
}
