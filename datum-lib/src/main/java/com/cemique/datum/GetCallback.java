package com.cemique.datum;

/**
 * For getting callback when the value is ready.
 */
public interface GetCallback<U>{

    void onGetValue(U u);

}
