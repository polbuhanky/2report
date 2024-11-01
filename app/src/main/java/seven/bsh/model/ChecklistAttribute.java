package seven.bsh.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ChecklistAttribute implements Serializable {
    public static final int TYPE_SEPARATOR = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_COMMENT = 2;
    public static final int TYPE_HEADER = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_NUMBER = 5;
    public static final int TYPE_CHECKBOX = 6;
    public static final int TYPE_SELECT = 7;
    public static final int TYPE_MULTI_SELECT = 8;
    public static final int TYPE_DATETIME = 9;
    public static final int TYPE_IMAGE = 10;
    public static final int TYPE_FILE = 11;
    public static final int TYPE_PRICE = 12;
    public static final int TYPE_SKU = 256;
    public static final int TYPE_WEEK = 257;

    private static final String TAG = "ChecklistAttributeModel";

    private int mType;
    private JSONObject mData;
    private JSONObject mSettings;

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public void parse(JSONObject data) throws JSONException {
        mData = data;
        mType = data.getInt("type");
        mSettings = data.optJSONObject("settings");
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getType() {
        return mType;
    }

    public JSONObject getData() {
        return mData;
    }

    public void setData(JSONObject data) {
        this.mData = data;
    }

    public void setData(String rawJson) {
        try {
            if (rawJson != null) {
                mData = new JSONObject(rawJson);
            }
        } catch (JSONException ex) {
            Log.e(TAG, ex.toString(), ex);
        }
    }

    public JSONObject getSettings() {
        return mSettings;
    }
}
