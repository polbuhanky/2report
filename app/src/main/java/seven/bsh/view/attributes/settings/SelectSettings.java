package seven.bsh.view.attributes.settings;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.R;

public class SelectSettings extends DefaultSettings {
    private final Context mContext;
    private String[] mItems;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SelectSettings(Context context) {
        mContext = context;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(JSONObject settings) throws JSONException {
        super.parse(settings);
        JSONArray listData = settings.getJSONArray("items");
        int dataLength = listData.length();
        mItems = new String[dataLength + 1];
        mItems[0] = mContext.getString(R.string.attribute_select_empty);

        for (int i = 0; i < dataLength; i++) {
            mItems[i + 1] = listData.getString(i);
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
