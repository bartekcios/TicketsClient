package com.bartekcios.ticketsclient;

/**
 * Created by bartekcios on 2017-01-09.
 * Class contains static fields of user data
 */

class User {
    public static String token = "";
    public static String username = "";
    public static String password = "";
    public static String firstName = "";
    public static String lastName = "";
    public static String email = "";
    public static boolean initialized = false;
    public static boolean loggedIn = false;

    public static void reset()
    {
        token = "";
        username = "";
        password = "";
        firstName = "";
        lastName = "";
        email = "";
        initialized = false;
        loggedIn = false;
    }
}
