package com.example.loginscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class CipherActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext;
    private EditText EdtMessage;
    private TextView TvEncryptmessage, TvDecryptmessage;
    private Button BtnEncrypt, BtnDecrypt;
    private String EnterString, EncryptedMessage, DecryptedMessage;
    private static final int pswdIterations = 10;
    private static final int keySize = 256;
    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String plainText = "sampleText";
    private static final String AESSalt = "exampleSalt";
    private static final String initializationVector = "8119745113154120";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cipher);
        mContext = CipherActivity.this;

        EdtMessage = findViewById(R.id.edt_message);
        TvEncryptmessage = findViewById(R.id.tv_encryptmessage);
        TvDecryptmessage = findViewById(R.id.tv_decryptmessage);
        BtnEncrypt = findViewById(R.id.btn_encrypt);
        BtnDecrypt = findViewById(R.id.btn_decrypt);

        BtnEncrypt.setOnClickListener(this);
        BtnDecrypt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_encrypt:
                if (EdtMessage.getText().toString().trim().isEmpty() ||	EdtMessage.getText().toString().equals(" ") || EdtMessage.getText().toString().length() ==0)
                {
                    Toast.makeText(mContext, "Please Enter Message First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    EnterString = EdtMessage.getText().toString().trim();
                    try {
                        EncryptedMessage = encrypt(EnterString);
                        TvEncryptmessage.setText(EncryptedMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.btn_decrypt:
                if (EncryptedMessage==null)
                {
                    Toast.makeText(mContext, "Please Enter Message or Encrypted Message First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        DecryptedMessage = decrypt(EncryptedMessage);
                        TvDecryptmessage.setText(DecryptedMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    // TODO For String Message Encryption
    public static String encrypt(String textToEncrypt) throws Exception
    {
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new     IvParameterSpec(initializationVector.getBytes()));
        byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    // TODO Decryption of Encrypted Message
    public static String decrypt(String textToDecrypt) throws Exception
    {
        byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decrypted = cipher.doFinal(encryted_bytes);
        return new String(decrypted, "UTF-8");
    }

    // TODO For Generate Secret Key For Encryption and Decryption of Message
    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
