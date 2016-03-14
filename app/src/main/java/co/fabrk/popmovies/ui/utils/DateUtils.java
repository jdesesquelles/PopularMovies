package co.fabrk.popmovies.ui.utils;

import android.text.format.Time;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ebal on 23/12/15.
 */
public class DateUtils {

    public static long normalizeDate(long releaseDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(releaseDate);
        int julianDay = Time.getJulianDay(releaseDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /**
     * Format Date to the US format.
     *
     * @param date       Date to format
     */
    public static String formatDate(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(5, 7)));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8, 10)));

        date = calendar.get(Calendar.DAY_OF_MONTH)
                + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)
                + " " + calendar.get(Calendar.YEAR);
        return date;
    }
}
