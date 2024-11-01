package seven.bsh.view.attributes.values;

import android.content.Context;

import seven.bsh.R;

public class PriceInputValue extends NumberInputValue {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public PriceInputValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean validate() {
        String value = getValue();
        if ((settings.isRequired() || !value.isEmpty()) && !value.matches("\\d+([.]\\d{1,2})?")) {
            error = getContext().getString(R.string.attribute_error_empty);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        String value = getValue();
        if (value.isEmpty()) {
            return null;
        }
        return Double.valueOf(value);
    }
}
