package com.bartekcios.ticketsclient;

/**
 * Created by bartekcios on 2017-01-09.
 */

public class User {
    public static String mToken = "";
    public static String mUsername = "";
    public static String mPassword = "";
    public static int mPasswordLength = 0;
    public static String mFirstName = "";
    public static String mLastName = "";
    public static String mEmail = "";
    public static boolean mInitialized = false;
    public static boolean mLoggedIn = false;

    public static void deinit()
    {
        mToken = "";
        mUsername = "";
        mPassword = "";
        mPasswordLength = 0;
        mFirstName = "";
        mLastName = "";
        mEmail = "";
        mInitialized = false;
        mLoggedIn = false;
    }
}
