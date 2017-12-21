package com.sylq.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public enum LogLevel {
        ERROR, WARN, INFO
    }

    public static LogLevel APPLICATION_LOG_LEVEL = LogLevel.ERROR;
    public static final DateFormat LOCALE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private static final DateFormat DATABASE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Convert a string from DATABASE_FORMAT to java.util.Date
     * @param date string date (format yyyy-MM-dd HH:mm:ss)
     * @return A Date object.
     */
    public static Date databaseDate(String date) {
        try {
            return DATABASE_FORMAT.parse(date);
        } catch (ParseException e) {
            warn("Attempted to parse null");
        }
        return null;
    }

    /**
     * Convert a Date object to DATABASE_FORMAT String.
     * @param date Date object
     * @return A Date string (format yyyy-MM-dd HH:mm:ss).
     */
    public static String stringifyDate(Date date) {
        try {
            return DATABASE_FORMAT.format(date);
        }
        catch (NullPointerException e) {
            warn("Attempted to stringify null");
        }
        return null;
    }

    public static void info(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.INFO.ordinal()) {
            String SET_BLUE = "\u001B[34m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_BLUE + content + SET_DEFAULT);
        }
    }

    public static void warn(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.WARN.ordinal()) {
            String SET_YELLOW = "\u001B[33m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_YELLOW + content + SET_DEFAULT);
        }
    }

    public static void error(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.ERROR.ordinal()) {
            String SET_RED = "\u001B[31m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_RED + content + SET_DEFAULT);
        }
    }
}
