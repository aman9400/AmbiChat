package com.ambiguous.ambichat.api;

public class ApiVolleyRequest {

    private static final String baseUrl = "http://dev.ambiguousit.com/chatO2O/api/";

    public static final String checkContacts = baseUrl + "checkForContact.php";

    public static final String mailVerify = baseUrl  + "mailVerifier.php";

    public static final String registration = baseUrl + "registration.php";

    public static final String login = baseUrl + "login.php";

    public static final String splash = baseUrl + "config.php";

    public static final String logout = baseUrl + "logout.php";

    public static String forgotPassword = baseUrl + "forgotPassword.php";

    public static String numberCheck = baseUrl + "numberCheck.php";

    public static final String setProfile = baseUrl + "setProfile.php";

    public static final String allMessage = baseUrl + "allMessage.php";

    public static final String changePassword = baseUrl + "changePassword.php";

    public static final String singleMessage = baseUrl + "singleMessage.php";

    public static final String sendMessage = baseUrl + "sendMessage.php";
}
