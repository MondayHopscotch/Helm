package com.bitdecay.helm.credits;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 4/9/2017.
 */

public class CreditsData {
    public String sectionTitle;
    public Array<CreditLine> creditLines;

    public CreditsData() {
        // Here for JSON
    }

    public static class CreditLine {
        public String color;
        public String text;
        public String url;

        public CreditLine() {
            // Here for JSON
        }
    }
}
