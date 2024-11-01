package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class TextInputSettings extends DefaultSettings {
    private int mMin;
    private int mMax;
    private boolean mMultiline;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        mMin = settings.getInt("min");
        mMax = settings.getInt("max");
        mMultiline = settings.getInt("multiline") == 1;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getMin() {
        return mMin;
    }

    public int getMax() {
        return mMax;
    }

    public boolean isMultiline() {
        return mMultiline;
    }
}
