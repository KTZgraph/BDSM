package com.example.loginscreen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserData {
    //Singleton do loginu użytkownika,
    // żeby np stworzyć baze po rejestracji i logowaniu a nie
    // ciągle przekazywac przez wszystkei klasy

    private static UserData instance;
    private String hashUsername; // TO sobie moze byc surowe haslo po zalogowaniu bo jest do drugiej bazy i jak ktos podglada dane jak ktos sie zalogowal to i tak juz nie potrzebuje tego hasla
    // haslo do zabezpieczenia bazy TYLKO w spoczynku


    public void setHashUsername(String hashUsername) {
        this.hashUsername = hashUsername;
    }

    public void setHashUsernameFromRaw(String rawUsername) throws NoSuchAlgorithmException {
        this.hashUsername = SHA_512(rawUsername);
    }

    public String getDatabaseRawPassword() {
        return databaseRawPassword;
    }

    public void setDatabaseRawPassword(String databaseRawPassword) {
        this.databaseRawPassword = databaseRawPassword;
    }

    private String databaseRawPassword;

    private  UserData(String rawUsername ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.hashUsername = SHA_512(rawUsername);
    }

    public static UserData getInstance(String rawUsername) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (instance == null){
            synchronized (UserData.class) {
                if(instance == null){ // podwojny chceck w singletonie
                    instance = new UserData(rawUsername);
                }
            }
        }
        return instance;
    }

    public void removeData(){
        // TODO usuwać wartośc jak użytkownik się wylogowuje !!!!!!!!!!!!!!!!

        // do usuwania jak sie wyloguje/ wyrejestruje czy cos // TODO
        setDatabaseRawPassword("");
        setHashUsername("");
    }

    public String getHashUsername() {
        return hashUsername;
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
