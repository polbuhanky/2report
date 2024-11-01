package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class TextSettings implements IAttributeSettings {
    private String mText;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        mText = settings.getString("text");
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getText() {
        return mText;
    }
}
