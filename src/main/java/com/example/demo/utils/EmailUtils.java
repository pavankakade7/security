package com.example.demo.utils;

public class EmailUtils {
    public static String getEmailMessage(String name, String host, String key){
        return "Hello" + ",\n\nYour new account has been created. Please click on the link below to verify your account. \n\n" +
                getVerificationUrl(host,key) + "\n\n The Support Team";
    }


    public static String getResetPasswordMessage(String name, String host, String token){
        return "Hello" + ",\n\nWe got a request to reset your account password . Please click on the link below to verify your account. \n\n" +
                getResetPasswordUrl(host,token) + "\n\n The Support Team";
    }


    public static String getVerificationUrl(String host, String key) {
        return host + "/verify/account?key=" + key;
    }

    public static String getResetPasswordUrl(String host, String token) {
        return host + "/verify/password?token=" + token;
    }
}
