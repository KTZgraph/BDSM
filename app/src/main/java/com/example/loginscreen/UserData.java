package com.example.loginscreen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
    //Singleton do loginu użytkownika,
    // żeby np stworzyć baze po rejestracji i logowaniu a nie
    // ciągle przekazywac przez wszystkei klasy

    private static UserData instance = new UserData(); // TODO gwarancja ze mi utworzy singletona od razu po uruchomieniu apki
    private String hashUsername = ""; // TO sobie moze byc surowe haslo po zalogowaniu bo jest do drugiej bazy i jak ktos podglada dane jak ktos sie zalogowal to i tak juz nie potrzebuje tego hasla
   private String databaseRawPassword = "";

    // haslo do zabezpieczenia bazy TYLKO w spoczynku


    public void setHashUsername(String rawUsername) throws NoSuchAlgorithmException {
//        this.hashUsername = SHA_512(rawUsername);
        this.hashUsername = rawUsername;
    }

    public String getHashUsername() {
        return hashUsername;
    }


    public String getDatabaseRawPassword() {
        return databaseRawPassword;
    }

    public void setDatabaseRawPassword(String databaseRawPassword) {
        this.databaseRawPassword = databaseRawPassword;
    }

    private  UserData() {
        this.hashUsername = "";
        this.databaseRawPassword = "";
    }

    public static UserData getInstance() {
        if (instance == null){
            synchronized (UserData.class) {
                if(instance == null){ // podwojny chceck w singletonie
                    instance = new UserData();
                }
            }
        }
        return instance;
    }





    public static String SHA_512(String originalString) throws NoSuchAlgorithmException {
        //https://developer.android.com/reference/java/security/MessageDigest
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(originalString.getBytes());
        return result.toString();
    }

}
