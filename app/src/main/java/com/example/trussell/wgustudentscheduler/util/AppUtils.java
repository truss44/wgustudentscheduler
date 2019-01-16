package com.example.trussell.wgustudentscheduler.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {


    public static Date getCurrentDateTime(){
        return Calendar.getInstance().getTime();
    }

    public static String getFormattedDateString(Date date) {
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return spf.format(date);
    }

    public static Date formatStringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date newDate = null;

        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }

    public static Date formatDate(Date date) {
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String newDate = targetFormat.format(date);
        Date returnDate = null;

        try {
            returnDate = targetFormat.parse(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return returnDate;
    }

    public static boolean dateValidation(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        return pattern.matcher(date).matches();
    }

    public static boolean isValidEmail(String string){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    public static boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    public static boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

    public static void showShortMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}