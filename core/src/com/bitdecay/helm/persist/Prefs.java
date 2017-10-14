package com.bitdecay.helm.persist;

/**
 * Created by Monday on 12/26/2016.
 */
public interface Prefs {
    int getInt(String key);
    int setInt(String key, int value);
    int getString(String key);
    int setString(String key, String value);
}
