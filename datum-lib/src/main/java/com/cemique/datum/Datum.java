package com.cemique.datum;

/**
 * Super interface for user interaction with datums.
 */
public interface Datum<T> {

    /**
     * @return Key of the datum.
     */
    String getKey();

    /**
     * @return Default value defined for the datum.
     */
    T getDefaultValue();

}
