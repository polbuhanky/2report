package seven.bsh.view.attributes.values;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.R;
import seven.bsh.utils.Formatter;
import seven.bsh.view.attributes.settings.DateTimeSettings;

public class DateTimeValue extends BaseDataValue {
    private String mDate;
    private String mTime;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public DateTimeValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        try {
            JSONObject reportData = (JSONObject) data;
            if (reportData.has("date") && !reportData.isNull("date")) {
                mDate = reportData.getString("date");
            }

            if (reportData.has("time") && !reportData.isNull("time")) {
                mTime = reportData.getString("time");
                if (mTime.matches("^(\\d+:){2}\\d+$")) {
                    mTime = mTime.substring(0, 5) + ":00";
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean validate() {
        DateTimeSettings settings = (DateTimeSettings) this.settings;
        Context context = getContext();
        if (settings.isRequired()) {
            if ((settings.hasDate() && mDate == null) || (settings.hasTime() && mTime == null)) {
                error = context.getString(R.string.attribute_error_empty);
                return false;
            }
        } else if (settings.hasDate() && settings.hasTime()) {
            if ((mDate == null && mTime != null) || (mDate != null && mTime == null)) {
                error = context.getString(R.string.attribute_error_empty);
                return false;
            }
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        DateTimeSettings settings = (DateTimeSettings) this.settings;
        JSONObject params = new JSONObject();
        try {
            if (settings.hasTime()) {
                params.put("time", mTime);
            }
            if (settings.hasDate()) {
                params.put("date", mDate);
            }
            return params;
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
            return null;
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getTime() {
        return mTime;
    }

    public String getFormattedTime() {
        if (mTime == null) {
            return "";
        }
        return mTime.substring(0, 5);
    }

    public String getFormattedDate() {
        if (mDate == null) {
            return "";
        }
        return Formatter.convertDateString(mDate, "yyyy-MM-dd", "dd.MM.yyyy");
    }

    public void setTime(String time) {
        mTime = time;
    }

    @Override
    public boolean isChanged() {
        DateTimeSettings settings = (DateTimeSettings) this.settings;
        boolean hasChanged = false;
        if (settings.hasTime()) {
            hasChanged = mTime != null;
        }
        if (settings.hasDate()) {
            hasChanged |= mDate != null;
        }
        return hasChanged;
    }
}
