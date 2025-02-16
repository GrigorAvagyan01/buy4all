package com.example.buy4all4;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String LANGUAGE_KEY = "language";

    /**
     * Apply the saved language when the app starts.
     * This should be called in `onCreate()` of the main activity.
     */
    public static void setAppLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString(LANGUAGE_KEY, "en");
        setAppLanguage(context, languageCode);
    }

    /**
     * Change the app language dynamically and persist it.
     * @param context Application context.
     * @param languageCode The selected language code (e.g., "en", "ru", "hy").
     */
    public static void setAppLanguage(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Save the selected language to SharedPreferences
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(LANGUAGE_KEY, languageCode);
        editor.apply();
    }

    /**
     * Retrieve the currently selected language code.
     * @param context Application context.
     * @return The saved language code or "en" if not found.
     */
    public static String getSelectedLanguageCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE_KEY, "en");
    }
}
