package com.cemique.datum;

/**
 * Assert functions throwing exception when not satisfied.
 */
public class Assertion {

    /**
     * @param object This cannot be null.
     * @throws AssertionError
     */
    public static void assertNonNull(Object object) throws AssertionError{
        if(object==null)
            throw new AssertionError("Null isn't allowed!");
    }

}
