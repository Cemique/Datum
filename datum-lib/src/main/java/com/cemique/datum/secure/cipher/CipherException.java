package com.cemique.datum.secure.cipher;

/**
 * Includes all exceptions that occur while working with cipher.
 */
class CipherException extends RuntimeException{
    CipherException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}