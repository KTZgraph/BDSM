package com.example.loginscreen;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
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
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Note {
    private long ID;
    private String date;
    private String time;
    private String ciphertext;
    private SecretKey secret;
    private byte[] salt;
    private byte[] iv;

    Cipher ecipher;
    Cipher dcipher;
    int iterationCount = 19;


    Note(){}

    Note(String rawPassword, String content, String date, String time) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidAlgorithmParameterException {
        // NOWA notatka bez id
        this.date = date;
        this.time = time;
        this.salt = generateSalt();
//        encryptMessage(content);
        // tylko szyfrowanie
        String enc = encrypt(rawPassword, content);

        this.ciphertext = encrypt(rawPassword, content);
        Log.i("Note construtot: ", "Original text: "+content);
        Log.i("Note construtot: ", "Encrypted text: "+enc);

        String plainAfter= decrypt(rawPassword, enc);
        Log.i("Note construtot: ", "Encrypted text: "+enc);
        Log.i("Note construtot: ", "Original text after decryption: "+plainAfter);

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

    // HASlaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    public byte[] generateSalt() throws NoSuchAlgorithmException, InvalidKeySpecException {
        /* Derive the key, given password and salt. */
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        //ustawainia soli w obiekcie
        return salt;
    }




    //https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat/32583766
    public String encrypt(String secretKey, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException {
        //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        //Enc process
        ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        String charSet = "UTF-8";
        byte[] in = plainText.getBytes(charSet);
        byte[] out = ecipher.doFinal(in);
        String encStr = new String( //https://stackoverflow.com/questions/60785457/how-to-obtain-the-base64-representation-of-a-string-in-android-oreo
//                Base64.getEncoder().encode(out)
                Base64.encode(out, Base64.DEFAULT)
        );
        return encStr;
    }

    public String decrypt(String secretKey, String encryptedText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            UnsupportedEncodingException,
            IllegalBlockSizeException,
            BadPaddingException,
            IOException {
        //Key generation for enc and desc
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        //Decryption process; same key will be used for decr
        dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
//        byte[] enc = Base64.getDecoder().decode(encryptedText);
        byte[] enc = Base64.decode(encryptedText, Base64.DEFAULT);
        byte[] utf8 = dcipher.doFinal(enc);
        String charSet = "UTF-8";
        String plainStr = new String(utf8, charSet);
        return plainStr;
    }
}
