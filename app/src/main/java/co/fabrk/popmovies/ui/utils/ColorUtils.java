package co.fabrk.popmovies.ui.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.graphics.Palette;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import co.fabrk.popmovies.R;

/**
 * Utility class performing actions on colors.
 * <p>
 * Created by jeremie.
 */
public class ColorUtils {

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    public static int modifyAlpha(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    public static int modifyAlpha(@ColorInt int color,
                                  @FloatRange(from = 0f, to = 1f) float alpha) {
        return modifyAlpha(color, (int) (255f * alpha));
    }


    /**
     * Method returning given color modified to lighter color (positive translation) or darker
     * (negative translation).
     *
     * @param primaryColor basic color
     * @param translation  positive or negative value of color translation
     * @return lighter/darker color
     */
    public static int getColorWithTranslateBrightness(int primaryColor, int translation) {
        if (translation >= 0) {
            return Color.argb(Color.alpha(primaryColor),
                    Math.min(Color.red(primaryColor) + translation, 255),
                    Math.min(Color.green(primaryColor) + translation, 255),
                    Math.min(Color.blue(primaryColor) + translation, 255));
        } else {
            return Color.argb(Color.alpha(primaryColor),
                    Math.max(Color.red(primaryColor) + translation, 0),
                    Math.max(Color.green(primaryColor) + translation, 0),
                    Math.max(Color.blue(primaryColor) + translation, 0));
        }
    }


    /**
     * Method returning color with modified alpha proportional to given values.
     *
     * @param color     color to modify
     * @param fullValue total value
     * @param partValue part of fullValue. When partValue equals fullValue, the alpha is 255.
     * @return color with alpha relative to partValue/fullValue ratio.
     */
    public static int getColorWithProportionalAlpha(int color, float fullValue, float partValue) {
        float progress = Math.min(Math.max(partValue, 0), fullValue) / fullValue;
        return Color.argb(
                Math.round(Color.alpha(color) * progress),
                Color.red(color),
                Color.green(color),
                Color.blue(color));
    }


    /**
     * Method returning color between start and end color proportional to given values.
     *
     * @param colorStart start color
     * @param colorEnd   end color
     * @param fullValue  total value
     * @param partValue  part of fullValue. When partValue equals 0, returning color is colorStart,
     *                   when partValue is fullValue returning color is endColor. Otherwise returning
     *                   color is from between those, relative to partValue/fullValue ratio.
     * @return color from between start and end color relative to partValue/fullValue ratio.
     */
    public static int getProportionalColor(int colorStart, int colorEnd, float fullValue, float partValue) {
        float progress = Math.min(Math.max(partValue, 0f), fullValue) / fullValue;
        return Color.argb(
                Math.round(Color.alpha(colorStart) * (1 - progress) + Color.alpha(colorEnd) * progress),
                Math.round(Color.red(colorStart) * (1 - progress) + Color.red(colorEnd) * progress),
                Math.round(Color.green(colorStart) * (1 - progress) + Color.green(colorEnd) * progress),
                Math.round(Color.blue(colorStart) * (1 - progress) + Color.blue(colorEnd) * progress));
    }

    public static String colorKeyVibrant = "Vibrant";
    public static String colorKeyDarkVibrant = "DarkVibrant";
    public static String colorKeyLightVibrant = "LightVibrant";
    public static String colorKeyMuted = "Muted";
    public static String colorKeyDarkMuted = "DarkMuted";
    public static String colorKeyLightMuted = "LightMuted";

    public static ArrayMap<String, Integer> processPalette(Context context, Palette p) {
        ArrayMap<String, Integer> map = new ArrayMap<>();
        map.put(colorKeyVibrant, p.getVibrantColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        map.put(colorKeyDarkVibrant, p.getDarkVibrantColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        map.put(colorKeyLightVibrant, p.getLightVibrantColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        map.put(colorKeyMuted, p.getMutedColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        map.put(colorKeyDarkMuted, p.getDarkMutedColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        map.put(colorKeyLightMuted, p.getLightMutedColor(ContextCompat.getColor(context, R.color.details_rate_not_initialized_bg)));
        return map;
    }

    public static ArrayMap<String, Palette.Swatch> processSwatch(Palette p) {
        ArrayMap<String, Palette.Swatch> map = new ArrayMap<>();
        if (p.getVibrantSwatch() != null)
            map.put(colorKeyVibrant, p.getVibrantSwatch());
        if (p.getDarkVibrantSwatch() != null)
            map.put(colorKeyDarkVibrant, p.getDarkVibrantSwatch());
        if (p.getLightVibrantSwatch() != null)
            map.put(colorKeyLightVibrant, p.getLightVibrantSwatch());
        if (p.getMutedSwatch() != null)
            map.put(colorKeyMuted, p.getMutedSwatch());
        if (p.getDarkMutedSwatch() != null)
            map.put(colorKeyDarkMuted, p.getDarkMutedSwatch());
        if (p.getLightMutedSwatch() != null)
            map.put(colorKeyLightMuted, p.getLightMutedSwatch());
        return map;
    }

    /**
     * Method returning a color value animator object. The caller needs to add the updateListener,
     * override the onAnimationUpdate method and call start on the animation
     *
     * @param duration duration of the animation
     * @param startDelay  delay before starting the animation
     * @param property  property to be animated
     * @param colorStart  Color at the beginning of the animation
     * @param colorEnd  Color at the end of the animation
     * @return Value animator
     */

    public static ObjectAnimator animateObjectViewColor(int duration, int startDelay, View view, String property, int colorStart, int colorEnd) {
        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(
                view,
                property,
                colorStart,
                colorEnd);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setDuration(duration);
        colorAnimator.setStartDelay(startDelay);
        colorAnimator.setInterpolator(new AccelerateInterpolator());
        return colorAnimator;
        //TODO move in fragment
//        colorAnimator.start();
    }

    /**
     * Method returning a color value animator object. The caller needs to add the updateListener,
     * override the onAnimationUpdate method and call start on the animation
     *
     * @param duration duration of the animation
     * @param startDelay  delay before starting the animation
     * @param colorStart  Color at the beginning of the animation
     * @param colorEnd  Color at the end of the animation
     * @return Value animator
     */
    public static ValueAnimator animateColorValue(int duration, int startDelay, int colorStart, int colorEnd) {

        ValueAnimator colorAnimation = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorStart, colorEnd)
                .setDuration(duration - startDelay);
        colorAnimation.setInterpolator(new AccelerateInterpolator());
        return colorAnimation;
        //TODO move in fragment

//        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animator) {
////                viewHolder..setBackgroundColor((int) animator.getAnimatedValue());
//                getActivity().getWindow().setStatusBarColor((int) animator.getAnimatedValue());
//            }
//        });
//            colorAnimation.start();

    }

}