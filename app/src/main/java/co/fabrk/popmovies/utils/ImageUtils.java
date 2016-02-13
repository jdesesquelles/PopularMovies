package co.fabrk.popmovies.utils;

import android.graphics.Color;


/**
 * Created by ebal on 29/12/15.
 */
public class ImageUtils {

    public static int multiplyColor(int srcColor, float factor) {
        int alpha = Color.alpha(srcColor);
        int red = (int) (Color.red(srcColor) * factor);
        int green = (int) (Color.green(srcColor) * factor);
        int blue = (int) (Color.blue(srcColor) * factor);
        return Color.argb(alpha, red, green, blue);
    }
}
