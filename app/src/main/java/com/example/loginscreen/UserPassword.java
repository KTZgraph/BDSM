package com.example.loginscreen;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserPassword {
    public static Boolean comparePasswords(String hashPassword, String rawPassword){
        Log.i("WYWALIC TEN LOG: ", "hashPassword: " + hashPassword);
        Log.i("WYWALIC TEN LOG: ", "rawPassword: " + rawPassword);

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray()); //surowe hasło do bcrypta
        BCrypt.Result result = BCrypt.verifyer().verify(hashPassword.toCharArray(), bcryptHashString); // porownanie tranformacji z haslem z bazy

        if (result.verified == true) return true;
        else return false;

    }

    public static String getPasswordHash(String rawPassword){
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray()); //hashowanie hasła
        System.out.println(bcryptHashString);
        Log.i("WYWALIC TEN LOG: ", "HAsło: " + bcryptHashString);
        return bcryptHashString;
    }

    public static Boolean validatePasswordPolicy(String rawPassword){
        /* https://examples.javacodegeeks.com/core-java/util/regex/matcher/validate-password-with-java-regular-expression-example/
        * Be between 8 and 40 characters long
        * Contain at least one digit.
        * Contain at least one lower case character.
        * Contain at least one upper case character.
        * Contain at least on special character from [ @ # $ % ! . ].
        */

        final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
        Pattern pattern  = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher =  pattern.matcher(rawPassword);
        return matcher.matches();
    }


}
