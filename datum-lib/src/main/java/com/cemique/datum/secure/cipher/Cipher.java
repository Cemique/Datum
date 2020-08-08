package com.cemique.datum.secure.cipher;

/**
 * Interface for encrypting and decrypting string messages.
 * Ciphered messages must be in base64 format.
 */
public interface Cipher {

    /**
     * @param message Clean message to be encrypted.
     * @return Base64 ciphered message.
     */
    String encrypt(String message);

    /**
     * @param encryptedB64 Base64 ciphered message.
     * @return Decrypted message.
     */
    String decrypt(String encryptedB64);

    /**
     * @param message Clean message to be encrypted.
     * @return Same as encrypt(String message), but trimmed.
     */
    default String trimmedEncrypt(String message){
        return encrypt(message).trim();
    }

}