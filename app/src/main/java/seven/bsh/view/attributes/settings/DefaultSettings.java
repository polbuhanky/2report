package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class DefaultSettings implements IInputAttributeSettings {
    private String mName;
    private String mLabel;
    private boolean mRequired;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        mName = settings.getString("name");
        mRequired = settings.optInt("required") == 1;
        mLabel = settings.getString("label");
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getName() {
        return mName;
    }

    public String getLabel() {
        return mLabel;
    }

    public boolean isRequired() {
        return mRequired;
    }
}
