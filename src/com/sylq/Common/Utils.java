package com.sylq.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public enum LogLevel {
        QUIET, ERROR, WARN, INFO
    }

    public static LogLevel APPLICATION_LOG_LEVEL = LogLevel.ERROR;
    public static final DateFormat LOCALE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final String NL = System.getProperty("line.separator");
    private static final DateFormat DATABASE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Convert a string from DATABASE_FORMAT to java.util.Date
     * @param date string date (format yyyy-MM-dd HH:mm:ss)
     * @return A Date object.
     */
    public static Date databaseDate(String date) {
        try {
            return DATABASE_FORMAT.parse(date);
        } catch (ParseException | NullPointerException e) {
            return null;
        }
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
            return null;
        }
    }

    /**
     * Print a standard message
     * @param content The message to print
     */
    public static void print(String content) {
        String SET_BLUE = "\u001B[34m";
        String SET_DEFAULT = "\u001B[0m";
        System.out.println(SET_BLUE + NL + content + SET_DEFAULT);
    }

    /**
     * Print a success message
     * @param content The message to print
     */
    public static void success(String content) {
        String SET_CYAN = "\u001B[36m";
        String SET_DEFAULT = "\u001B[0m";
        System.out.println(SET_CYAN + content + NL + SET_DEFAULT);
    }

    /**
     * Print an informative message, only if application Log Level is LogLevel.INFO or higher.
     * @param content The message to print
     */
    public static void info(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.INFO.ordinal()) {
            String SET_GREEN = "\u001B[32m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_GREEN + content + NL + SET_DEFAULT);
        }
    }

    /**
     * Print a message only if application Log Level is LogLevel.WARN or higher.
     * @param content The message to print
     */
    public static void warn(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.WARN.ordinal()) {
            String SET_YELLOW = "\u001B[33m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_YELLOW + content + NL + SET_DEFAULT);
        }
    }

    /**
     * Print a message only if application Log Level is LogLevel.ERROR or higher.
     * @param content The message to print
     */
    public static void error(String content) {
        if (APPLICATION_LOG_LEVEL.ordinal() >= LogLevel.ERROR.ordinal()) {
            String SET_RED = "\u001B[31m";
            String SET_DEFAULT = "\u001B[0m";
            System.out.println(SET_RED + content + NL + SET_DEFAULT);
        }
    }

    /**
     * Check if a string is a "valid email".
     * (Fairly effective method, doesn't handle exotic addresses).
     * @param emailStr
     * @return Either true or false.
     */
    public static boolean emailIsValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
