package com.example.loginscreen;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
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
    private SecretKey secret;
    private byte[] salt;
    private String iv;

    Note(){}

    Note(String rawPassword, String content, String date, String time) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException {
        this.date = date;
        this.time = time;
        deriveKey(rawPassword);
        encryptMessage(content);
    }

    Note(long id, String rawPassword, String content, String date, String time) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException {
        //do edytowania notatek
        this.ID = id;
        this.date = date;
        this.time = time;
        deriveKey(rawPassword);
        encryptMessage(content);
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
        try {secret = KeyGenerator.getInstance("AES").generateKey();}
        catch (NoSuchAlgorithmException e) {/* LOG YOUR EXCEPTION */}

        if (secret != null){
            String stringKey;
            stringKey = Base64.encodeToString(secret.getEncoded(), Base64.DEFAULT);
            return stringKey;
        }
        return "Jakis babol w luczach";
    }

    public void setSecret(String stringKey) {
        // ustawiam klucz ze stringa - latwiej ze wzgldu na DB
        byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
        SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        this.secret = originalKey;
    }


    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String  getPlainText(){
        try{
            return decryptMessage();

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "JAKIE SBLEDY z AESEM";
    }

    // HASlaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    public void deriveKey(String rawPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /* Derive the key, given password and salt. */
        char[] password = rawPassword.toCharArray();
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);

        //ustawainia soli w obiekcie
        setSalt(salt); //TODO

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        //ustawainia HASLA obiekcie
        this.secret = secret; //bezposrednio bez konwersji //TODO
    }

    public void encryptMessage(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        /* Encrypt the message. */

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, this.secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        // ustawinianie vevtora inicjalizującego do bobiektu
        setIv(iv.toString()); //TODO

        // szyfrowanie notatki
        byte[] ciphertext = cipher.doFinal("Hello, World!".getBytes("UTF-8"));
//        byte[] ciphertext = cipher.doFinal(content.getBytes("UTF-8"));

        // ustawianie zaszyfrowanej wiadomosci w obiekcie

        setCiphertext(ciphertext.toString()); //TODO
    }

    public String decryptMessage() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        /* Decrypt the message, given derived key and initialization vector. */
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv.getBytes())); //TODO
        byte[] ciphertextByte = ciphertext.getBytes();
        String plaintext = new String(cipher.doFinal(ciphertextByte), "UTF-8");
        return plaintext;
    }
}
