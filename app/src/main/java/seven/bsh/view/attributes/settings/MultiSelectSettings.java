package seven.bsh.view.attributes.settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MultiSelectSettings extends DefaultSettings {
    private String[] mItems;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        JSONArray listData = settings.getJSONArray("items");
        mItems = new String[listData.length()];
        int length = mItems.length;
        for (int i = 0; i < length; i++) {
            mItems[i] = listData.getString(i);
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String[] getItems() {
        return mItems;
    }
}
