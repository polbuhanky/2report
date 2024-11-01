package seven.bsh.view.attributes.values;

import android.content.Context;

import seven.bsh.R;
import seven.bsh.view.attributes.settings.CheckboxSettings;

public class CheckboxValue extends BaseDataValue {
    private boolean mChecked;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CheckboxValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        mChecked = data.equals(1);
    }

    @Override
    public boolean validate() {
        boolean result = true;
        if (settings.isRequired()) {
            result = settings.isRequired() && mChecked;
            if (!result) {
                error = getContext().getString(R.string.attribute_error_empty);
            } else {
                error = null;
            }
        }
        return result;
    }

    @Override
    public Object serialize() {
        return mChecked ? 1 : 0;
    }

    @Override
    public void updateFromSettings() {
        super.updateFromSettings();
        mChecked = ((CheckboxSettings) settings).isChecked();
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @Override
    public boolean isChanged() {
        CheckboxSettings settings = (CheckboxSettings) this.settings;
        return mChecked != settings.isChecked();
    }
}
