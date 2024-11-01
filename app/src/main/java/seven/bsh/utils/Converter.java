package seven.bsh.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import java.nio.charset.Charset;

public class Converter {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public static int dip2Px(Context context, int dp) {
        return Math.round(dp * getPixelScaleFactor(context));
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
