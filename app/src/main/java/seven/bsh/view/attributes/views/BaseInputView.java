package seven.bsh.view.attributes.views;

import android.content.Context;
import android.widget.TextView;

import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.settings.DefaultSettings;
import seven.bsh.view.attributes.values.IDataValue;

public abstract class BaseInputView extends BaseStaticView {
    protected TextView errorField;

    private final IDataValue value;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseInputView(Context context, IInputAttribute attribute) {
        super(context, attribute);
        value = attribute.getValue();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    protected String getTitleLabel() {
        DefaultSettings settings = (DefaultSettings) getSettings();
        return settings.getLabel()
            + (settings.isRequired() ? "\u00A0*" : "");
    }

    protected String getString(int resourceId) {
        return getContext().getString(resourceId);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public IDataValue getValue() {
        return value;
    }
}
