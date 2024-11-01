package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class DateTimeSettings extends DefaultSettings {
    private boolean mDate;
    private boolean mTime;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        mTime = settings.getInt("time") == 1;
        mDate = settings.getInt("date") == 1;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean hasDate() {
        return mDate;
    }

    public boolean hasTime() {
        return mTime;
    }
}
