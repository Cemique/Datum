package com.cemique.datum.secure.cipher;

import android.util.Base64;

import androidx.annotation.Size;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Sample implementation of cipher {@link com.cemique.datum.secure.cipher.Cipher}
 * that uses 'AES/CBC/PKCS5Padding'. This is a {@link DeterministicCipher}.
 */
public class DefaultCipher implements com.cemique.datum.secure.cipher.DeterministicCipher {

    private String encoding;
    private SecretKey secretKey;
    private byte[] iv;

    private DefaultCipher(String encoding, String password, byte[] iv) {
        this.encoding = encoding;
        try {
            this.secretKey = new SecretKeySpec(md5(password), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new CipherException(e);
        }
        this.iv = iv;
    }

    public static class Builder {

        private String encoding = "UTF-8";
        private String password = "12345678";
        private byte[] iv = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};

        /**
         * @param encoding Encoding of clean messages.
         * @return This builder.
         */
        public Builder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        /**
         * @param password
         * @return This builder.
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * @param iv
         * @return This builder.
         */
        public Builder setIv(@Size(16) byte[] iv){
            this.iv = iv;
            return this;
        }

        /**
         * @return DefaultCipher.
         */
        public DefaultCipher build() {
            return new DefaultCipher(encoding, password, iv);
        }

    }

    public String encrypt(String message) throws CipherException{
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] messageBytes = message.getBytes(encoding);
            byte[] encryptedBytes = cipher.doFinal(messageBytes);
            byte[] encryptedB64Bytes = Base64.encode(encryptedBytes, Base64.DEFAULT);
            String encryptedB64String = new String(encryptedB64Bytes, encoding);
            return encryptedB64String;
        } catch (Exception e) {
            throw new CipherException(e);
        }
    }

    public String decrypt(String encryptedB64String) throws CipherException{
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedB64Bytes = encryptedB64String.getBytes(encoding);
            byte[] encryptedBytes = Base64.decode(encryptedB64Bytes, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String message = new String(decryptedBytes, encoding);
            return message;
        } catch (Exception e) {
            throw new CipherException(e);
        }
    }

    public static byte[] generateRandomIv(){
        byte[] iv = new byte[16];
        new Random().nextBytes(iv);
        return iv;
    }

    private byte[] md5(String string) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] stringBytes = string.getBytes();
        messageDigest.update(stringBytes);
        return messageDigest.digest();
    }

}
