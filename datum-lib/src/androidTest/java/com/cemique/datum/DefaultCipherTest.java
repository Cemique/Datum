package com.cemique.datum;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cemique.datum.secure.cipher.DefaultCipher;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DefaultCipherTest {

    @Test
    public void cipherTest() {
        for (int i = 0; i < 5; i++) {
            String message = getRandomString(getRandomInt(1, 100));
            String password = getRandomString(getRandomInt(1, 30));

            byte[] iv1 = DefaultCipher.generateRandomIv();
            DefaultCipher defaultCipher1 = new DefaultCipher.Builder().setPassword(password).setIv(iv1).build();
            String encryptedMessage1 = defaultCipher1.encrypt(message);
            String decryptedMessage1 = defaultCipher1.decrypt(encryptedMessage1);
            assertNotEquals(message, encryptedMessage1);
            assertEquals(message, decryptedMessage1);

            byte[] iv2 = DefaultCipher.generateRandomIv();
            DefaultCipher defaultCipher2 = new DefaultCipher.Builder().setPassword(password).setIv(iv2).build();
            String encryptedMessage2 = defaultCipher2.encrypt(message);
            String decryptedMessage2 = defaultCipher2.decrypt(encryptedMessage2);
            assertNotEquals(message, encryptedMessage2);
            assertEquals(message, decryptedMessage2);

            assertNotEquals(encryptedMessage1, encryptedMessage2);

            DefaultCipher defaultCipher3 = new DefaultCipher.Builder().setPassword(password).setIv(iv1).build();
            String encryptedMessage3 = defaultCipher3.encrypt(message);
            String decryptedMessage3 = defaultCipher3.decrypt(encryptedMessage3);
            assertNotEquals(message, encryptedMessage3);
            assertEquals(message, decryptedMessage3);

            assertEquals(encryptedMessage1, encryptedMessage3);
        }
    }

    private int getRandomInt(int from, int to) {
        return new Random().nextInt(to - from) + from;
    }

    private String getRandomString(int length) {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"//Uppercase letters
                + "abcdefghijklmnopqrstuvxyz"//Lowercase letters
                + "ضصثقفغعهخحجچشسیبلاتنمکگظطزرذدئوپ"//Persian letters
                + "۱۲۳۴۵۶۷۸۹۰"//Persian digits
                + "1234567890"//English digits
                + "`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?"//Symbols
                ;

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

}
