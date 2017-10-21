package com.bitdecay.helm.desktop;

import com.bitdecay.helm.external.URLOpener;

/**
 * Created by Monday on 10/21/2017.
 */

public class DesktopURLOpener implements URLOpener {
    @Override
    public void open(String url) {
        // no-op on desktop
        System.out.println("Opening URL: " + url);
    }
}
