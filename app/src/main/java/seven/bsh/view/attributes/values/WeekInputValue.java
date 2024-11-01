package seven.bsh.view.attributes.values;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import seven.bsh.R;

public class WeekInputValue extends BaseDataValue {
    private DateTime mDate;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public WeekInputValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        if (data == null) {
            return;
        }

        String value = (String) data;
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        mDate = DateTime.parse(value, format);
    }

    @Override
    public boolean validate() {
        if (settings.isRequired() && mDate == null) {
            error = getContext().getString(R.string.attribute_error_empty);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        if (mDate == null) {
            return null;
        }

        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        return mDate.toString(format);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public DateTime getDate() {
        return mDate;
    }

    public void setDate(DateTime date) {
        mDate = date;
    }

    @Override
    public boolean isChanged() {
        return mDate != null;
    }
}
