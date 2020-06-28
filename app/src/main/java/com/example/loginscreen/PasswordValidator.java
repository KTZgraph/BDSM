package com.example.loginscreen;

public class PasswordValidator {

    public static boolean valid(String password){
        String specialCharRegex= ".*[@#!$%^&+=].*";
        String UpperCaseRegex= ".*[A-Z].*";
        String NumberRegex= ".*[0-9].*";

        if(password.length() <10 ){
            return false;
        }

        if(!password.matches(specialCharRegex)){
            return false;
        }

        if(!password.matches(UpperCaseRegex)){
            return false;
        }

        if(!password.matches(NumberRegex)){
            return false;
        }

        return true;
    }
}
