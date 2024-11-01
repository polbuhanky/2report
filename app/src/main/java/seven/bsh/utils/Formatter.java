package seven.bsh.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import seven.bsh.R;

public class Formatter {
    public static String getDistanceMetric(Context context, int meter) {
        if (meter >= 1000) {
            return context.getString(R.string.activity_trade_objects_label_distance_km);
        }
        return context.getString(R.string.activity_trade_objects_label_distance_m);
    }

    public static String int2Hex(Context context, int colorResource) {
        int color = ContextCompat.getColor(context, colorResource);
        return Integer.toString(color & 0xFFFFFF, 16);
    }

    /**
     * Преобразовать строку с датой в другой формат
     *
     * @param value Строковое предтавление даты
     * @param from Формат входящей даты
     * @param to Формат выходящей даты
     */
    public static String convertDateString(String value, String from, String to) {
        try {
            if (value != null && !value.isEmpty()) {
                SimpleDateFormat fromFormat = new SimpleDateFormat(from, Locale.getDefault());
                SimpleDateFormat toFormat = new SimpleDateFormat(to, Locale.getDefault());
                Date date = fromFormat.parse(value);
                return toFormat.format(date);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
