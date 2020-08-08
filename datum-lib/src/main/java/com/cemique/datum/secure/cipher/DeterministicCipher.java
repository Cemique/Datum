package com.cemique.datum.secure.cipher;

/**
 * Subclasses need to implement {@link Cipher} in a deterministic way,
 * meaning that {@link Cipher#encrypt(String)} must return the same
 * cipher for the same message.
 */
public interface DeterministicCipher extends Cipher{
}