package seven.bsh.view.attributes;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import seven.bsh.view.attributes.fields.WeekInputField;
import seven.bsh.view.attributes.settings.DefaultSettings;
import seven.bsh.view.attributes.values.WeekInputValue;
import seven.bsh.view.attributes.views.WeekView;

public class WeekAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public WeekAttribute(Context context) {
        super(context, new DefaultSettings(), new WeekInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public String getWeek(DateTime date) {
        DateTimeFormatter format = DateTimeFormat.forPattern("dd.MM.yyyy");
        String text = date.toString(format) + " - ";
        date = date.plusDays(6);
        return text + date.toString(format);
    }

    @Override
    public void initField() {
        setField(new WeekInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new WeekView(getContext(), this));
    }
}
