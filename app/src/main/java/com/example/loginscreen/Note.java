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
import javax.crypto.KeyGenerator;
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
    private SecretKey secretKey;
    private byte[] salt;
    private byte[] iv;

    Cipher ecipher;
    Cipher dcipher;
    int iterationCount = 19;


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

    public void update(String newRawPassword, String OldRawPassword, String newContent) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if (newRawPassword.isEmpty()){
            this.ciphertext = encrypt(OldRawPassword, newContent);

        }else{
            this.salt = generateSalt();
            setSecret(newRawPassword);
            this.ciphertext = encrypt(newRawPassword, newContent);
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

    public String getSecret() {
        // zwracam Stringa latwiej ze wzgldu na DB
        // https://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
        String stringKey;

        try {secretKey = KeyGenerator.getInstance("AES").generateKey();}
        catch (NoSuchAlgorithmException e) {/* LOG YOUR EXCEPTION */}

        if (secretKey != null) {
            stringKey = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
            return stringKey;
        }
        return "Jakis babol w luczach";
    }


    public void setSecret(String stringKey) {
        // ustawiam klucz ze stringa - latwiej ze wzgldu na DB
        byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
        SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        this.secretKey = originalKey;
    }


    public void setSecret(SecretKeySpec secretKey) {
        this.secretKey = secretKey;
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
            return decrypt(rawPassword, getCiphertext());

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


    //https://howtodoinjava.com/security/aes-256-encryption-decryption/
    public String encrypt(String rawPassword, String strToEncrypt)
    {
        /*Java program to encrypt a password (or any information) using AES 256 bits.

        Po co w ogóle iv
        https://stackoverflow.com/questions/31132162/what-size-of-initialization-vector-needed-for-aes-256-encryption-in-java/31147653#31147653
        */
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            setIv(ivspec.getIV()); //zapisuje iv

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), getSalt(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            setSecret(secretKey);

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

    public String decrypt(String rawPassword, String strToDecrypt) {
        // Padding http://www.herongyang.com/Cryptography/DES-JDK-What-Is-PKCS5Padding.html
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), getSalt(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,  Base64.DEFAULT)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
