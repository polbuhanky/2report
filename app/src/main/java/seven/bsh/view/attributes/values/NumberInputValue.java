package seven.bsh.view.attributes.values;

import android.content.Context;

import org.json.JSONObject;

import seven.bsh.R;
import seven.bsh.view.attributes.settings.NumberInputSettings;

public class NumberInputValue extends BaseDataValue {
    private String mValue = "";

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public NumberInputValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        if (data != null && data != JSONObject.NULL) {
            mValue = String.valueOf(data);
        }
    }

    @Override
    public boolean validate() {
        NumberInputSettings settings = (NumberInputSettings) this.settings;
        Context context = getContext();
        if (settings.isRequired()) {
            if (settings.isFloat()) {
                if (!mValue.matches("\\d+([.]\\d+)?")) {
                    error = context.getString(R.string.attribute_error_empty);
                    return false;
                }
            } else if (mValue.isEmpty()) {
                error = context.getString(R.string.attribute_error_empty);
                return false;
            }
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        NumberInputSettings settings = (NumberInputSettings) this.settings;
        if (mValue.isEmpty()) {
            return null;
        }
        if (settings.isFloat()) {
            return Double.valueOf(mValue);
        }
        return Integer.valueOf(mValue);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    @Override
    public boolean isChanged() {
        return !mValue.isEmpty();
    }
}
