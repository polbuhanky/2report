package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckboxSettings extends DefaultSettings {
    private boolean mChecked;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        mChecked = settings.getInt("checked") == 1;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean isChecked() {
        return mChecked;
    }
}
