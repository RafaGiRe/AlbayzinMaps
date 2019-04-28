package com.tutorial.rafagire.albayzinmaps;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {
    public static void changeLocale(Resources res, String locale){
        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch(locale){
            case "es":
                config.locale = new Locale("es");
                break;
            case "en":
                config.locale = Locale.ENGLISH;
                break;
            default:
                config.locale = new Locale("es");
                break;
        }

        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}
