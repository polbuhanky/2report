package seven.bsh.view.attributes.values;

import android.content.Context;

import seven.bsh.R;

public class SelectValue extends BaseDataValue {
    private int mIndex = -1;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SelectValue(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void parse(Object data) {
        mIndex = (int) data;
    }

    @Override
    public boolean validate() {
        if (settings.isRequired() && mIndex == -1) {
            error = getContext().getString(R.string.attribute_select_error_empty);
            return false;
        }

        error = null;
        return true;
    }

    @Override
    public Object serialize() {
        if (mIndex == -1) {
            return null;
        }
        return mIndex;
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    @Override
    public boolean isChanged() {
        return mIndex != -1;
    }
}
