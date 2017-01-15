package com.bitdecay.game.time;

/**
 * Created by Monday on 1/15/2017.
 */
public class TimerUtils {

    public static String getFormattedTime(float timeInSeconds) {
        int secondsAsInt = (int) timeInSeconds;
        int minutes = secondsAsInt / 60;
        int seconds = secondsAsInt - (minutes * 60);
        int fractional = (int) ((timeInSeconds - secondsAsInt) * 1000);
        if (minutes != 0) {
            return String.format("%02d:%02d.%03d", minutes, seconds, fractional);
        } else {
            return String.format("%02d.%03d", seconds, fractional);
        }
    }
}
