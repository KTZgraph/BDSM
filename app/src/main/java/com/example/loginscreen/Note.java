package com.example.loginscreen;

import android.util.Base64;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Note {
    private long ID;
    private String date;
    private String time;
    private String ciphertext;
    private SecretKey secretPBKDF2Key; //powinien byc PBKDF2WithHmacSHA256 bo jest z sola i w ogole
    private byte[] salt;
    private byte[] iv;
    private int iterationCountPBKDF2WithHmacSHA256 = 64000; // Powinno byc 256000; ale wtedy emulator umiera :<

            /*https://crypto.stackexchange.com/questions/3484/pbkdf2-and-salt
            First, realize that PBKDF2 is PKCS #5 is RFC 2898, i.e. http://www.ietf.org/rfc/rfc2898.txt

            It's essentially an algorithm to securely hash a password as many times as you want, with whatever hash you want.
            OWASP recommends hashing the password at least 64,000 times in 2012, and doubling that every two years, per https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet
            https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
            2020 - 2012 = 8
            8/ 2 = 4
            4 * 64 000 =  256 000‬
             */






    Note(){}

    Note(String rawPassword, String content, String date, String time) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        this.date = date;
        this.time = time;
        this.salt = generateSalt();
        this.ciphertext = encrypt(rawPassword, content);
    }

    Note(long id, String rawPassword, String content, String date, String time) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        //do edytowania notatek; istniejąca notatka
        this.ID = id;
        this.date = date;
        this.time = time;
        this.ciphertext = encrypt(rawPassword, content); //szyfruje tresc notatki
    }

    public void update(String newRawPassword, String OldRawPassword, String rawNewContent) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (newRawPassword.isEmpty()){
            this.ciphertext = encrypt(OldRawPassword, rawNewContent);

        }else{
            this.salt = generateSalt();
            this.ciphertext = encrypt(newRawPassword, rawNewContent);
        }
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String setSecretPBKDF2Key() {
        // zwracam Stringa latwiej ze wzgldu na DB
        // https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa

        if (secretPBKDF2Key != null) { //POROWNUJE MOJ KLUCZ Z BAZY
            return  Base64.encodeToString(secretPBKDF2Key.getEncoded(), Base64.DEFAULT);
        }
        return "Jakis babol w luczach";
    }


    public void setSecretPBKDF2Key(String stringKey) {
        // ustawiam klucz ze stringa - latwiej ze wzgldu na DB
        byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
        SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        this.secretPBKDF2Key = originalKey;
    }
    

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String  getPlainText(String rawPassword){
        try{
            String rawContent = decrypt(rawPassword, getCiphertext());
                return rawContent;

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "JAKIE SBLEDY z AESEM";
    }

    public byte[] generateSalt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        /* Derive the key, given password and salt. */
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        //ustawainia soli w obiekcie
        return salt;
    }

    private Boolean checkOldPassword(String rawOldPassword) throws NoSuchAlgorithmException, InvalidKeySpecException { // TODO
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        KeySpec spec = new PBEKeySpec(rawOldPassword.toCharArray(), getSalt(), 65536, 256);
//        SecretKey tmp = factory.generateSecret(spec);
//        SecretKeySpec secretPBKDF2Key = new SecretKeySpec(tmp.getEncoded(), "AES");

        return true;
    }

    //https://howtodoinjava.com/security/aes-256-encryption-decryption/
    private String encrypt(String rawPassword, String strToEncrypt)
    {
        /*Java program to encrypt a password (or any information) using AES 256 bits.

        Po co w ogóle iv
        https://stackoverflow.com/questions/31132162/what-size-of-initialization-vector-needed-for-aes-256-encryption-in-java/31147653#31147653


        https://crypto.stackexchange.com/questions/3484/pbkdf2-and-salt
            First, realize that PBKDF2 is PKCS #5 is RFC 2898, i.e. http://www.ietf.org/rfc/rfc2898.txt

            It's essentially an algorithm to securely hash a password as many times as you want, with whatever hash you want.
            OWASP recommends hashing the password at least 64,000 times in 2012, and doubling that every two years, per https://www.owasp.org/index.php/Password_Storage_Cheat_Sheet
            https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
        */
        try
        {
            // https://medium.com/@nipun.357/aes-encryption-decryption-java-python-6e9f261c24d6 generowanie losowego IV
            // Generating random IV
            byte[] ivCode = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(ivCode);

            IvParameterSpec ivspec = new IvParameterSpec(ivCode);
            setIv(ivspec.getIV()); //zapisuje iv

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), getSalt(), this.iterationCountPBKDF2WithHmacSHA256, 256);
            setSecretPBKDF2Key(spec.toString()); // przechowuje jako string bo baza ogarnia

            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //tu mozna tryby zmienic o fajnie
            // który padding wybrać o.Ó ?
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
//            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes()), Base64.DEFAULT); //androidowe do Stringa
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    private String decrypt(String rawPassword, String strToDecrypt) {
        // Padding http://www.herongyang.com/Cryptography/DES-JDK-What-Is-PKCS5Padding.html
        try
        {
             // pobiera Iv z bazy
            IvParameterSpec ivspec = new IvParameterSpec(getIv());

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            /*
            https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html
            PBKDF2 can be used with HMACs based on a number of different hashing algorithms. HMAC-SHA-256 is widely supported and is recommended by NIST.
            The work factor for PBKDF2 is implemented through the iteration count, which should be at least 10,000 (although values of up to 100,000 may be appropriate in higher security environments).


             Argon2id
            Argon2 is the winner of the 2015 Password Hashing Competition. There are three different versions of the algorithm, and the Argon2id variant should be used where available, as it provides a balanced approach to resisting both side channel and GPU-based attacks.
               [!] Wow ten algrytm wygral, ale jest 4:45 to olewam YOLO :D

             */
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), getSalt(), this.iterationCountPBKDF2WithHmacSHA256, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,  Base64.DEFAULT)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }
}
