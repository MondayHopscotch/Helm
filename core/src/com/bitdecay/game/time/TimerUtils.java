package com.bitdecay.game.time;

/**
 * Created by Monday on 1/15/2017.
 */
public class TimerUtils {

    public static String getFormattedTime(float timeInSeconds) {
        int secondsAsInt = (int) timeInSeconds;
        int fractional = (int) ((timeInSeconds - secondsAsInt) * 1000);
        int hours = secondsAsInt / 3600;
        secondsAsInt -= hours * 3600;
        int minutes = secondsAsInt / 60;
        secondsAsInt -= minutes * 60;
        int seconds = secondsAsInt;
        if (hours != 0) {
            return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, fractional);
        } else if (minutes != 0) {
            return String.format("%02d:%02d.%03d", minutes, seconds, fractional);
        } else {
            return String.format("%02d.%03d", seconds, fractional);
        }
    }
}
