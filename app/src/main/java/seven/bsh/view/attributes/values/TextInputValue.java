package seven.bsh.view.attributes.values;

import android.content.Context;

import org.json.JSONObject;

import seven.bsh.R;
import seven.bsh.view.attributes.settings.TextInputSettings;

public class TextInputValue extends BaseDataValue {
    private String mValue = "";

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TextInputValue(Context context) {
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
            mValue = (String) data;
        }
    }

    @Override
    public boolean validate() {
        TextInputSettings settings = (TextInputSettings) this.settings;
        Context context = getContext();
        int length = mValue.length();
        int min = settings.getMin();
        int max = settings.getMax();

        if (settings.isRequired() && mValue.isEmpty()) {
            error = context.getString(R.string.attribute_error_empty);
            return false;
        } else if (min > length) {
            error = context.getString(R.string.attribute_text_error_too_small, min);
            return false;
        } else if (max > 0 && max < length) {
            error = context.getString(R.string.attribute_text_error_too_big, max);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        return mValue;
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
