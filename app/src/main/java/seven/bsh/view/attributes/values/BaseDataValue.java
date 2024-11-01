package seven.bsh.view.attributes.values;

import android.content.Context;

import seven.bsh.view.attributes.settings.IInputAttributeSettings;

public abstract class BaseDataValue implements IDataValue {
    protected static final String TAG = "DataValue";

    protected String error;
    protected IInputAttributeSettings settings;

    private Context mContext;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseDataValue(Context context) {
        mContext = context;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void updateFromSettings() {
        // ignored
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getError() {
        return error;
    }

    public void setSettings(IInputAttributeSettings settings) {
        this.settings = settings;
    }

    public Context getContext() {
        return mContext;
    }
}
