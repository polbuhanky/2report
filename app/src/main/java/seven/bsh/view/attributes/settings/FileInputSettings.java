package seven.bsh.view.attributes.settings;

import org.json.JSONException;
import org.json.JSONObject;

public class FileInputSettings extends DefaultSettings {
    private int mMaxSize;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        mMaxSize = settings.getInt("maxSize");
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getMaxSize() {
        return mMaxSize;
    }
}
