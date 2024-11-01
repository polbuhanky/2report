package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.db.DatabaseHelper;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.settings.IInputAttributeSettings;
import seven.bsh.view.attributes.values.IDataValue;

public abstract class BaseInputField implements IFieldView {
    private final Context context;
    private final IInputAttribute attribute;
    private final IInputAttributeSettings settings;
    private final IDataValue value;

    protected TextView errorField;
    protected boolean hasErrors;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseInputField(Context context, IInputAttribute attribute) {
        this.context = context;
        this.attribute = attribute;
        settings = (IInputAttributeSettings) attribute.getSettings();
        value = attribute.getValue();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public abstract void prepareDataValue();

    @Override
    public void applyValidation() {
        setError(value.getError());
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected String getTitleLabel() {
        String label = settings.getLabel();
        if (settings.isRequired()) {
            label += "\u00A0*";
        }
        return label;
    }

    protected void setError(int stringId, Object... args) {
        setError(getString(stringId, args));
    }

    protected void setError(String error) {
        if (error != null && !hasErrors) {
            errorField.setVisibility(View.VISIBLE);
            errorField.setText(error);
            hasErrors = true;
        } else if (error == null && hasErrors) {
            errorField.setVisibility(View.GONE);
            hasErrors = false;
        }
    }

    protected String getString(int stringId, Object... args) {
        return context.getString(stringId, args);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public Context getContext() {
        return context;
    }

    public IInputAttributeSettings getSettings() {
        return settings;
    }

    public IDataValue getValue() {
        return value;
    }

    public IInputAttribute getAttribute() {
        return attribute;
    }

    protected DatabaseHelper getDb() {
        return DatabaseHelper.getInstance();
    }
}
