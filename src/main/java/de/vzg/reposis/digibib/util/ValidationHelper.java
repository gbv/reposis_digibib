package de.vzg.reposis.digibib.util;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class ValidationHelper {

    public static boolean validateURL(final String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }
}
