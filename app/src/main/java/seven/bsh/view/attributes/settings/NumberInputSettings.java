package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class NumberInputSettings extends DefaultSettings {
    private boolean mFloat;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        mFloat = settings.getInt("float") == 1;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean isFloat() {
        return mFloat;
    }
}
