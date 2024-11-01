package seven.bsh.view.attributes;

import android.content.Context;
import android.view.View;

import seven.bsh.view.attributes.fields.BaseInputField;
import seven.bsh.view.attributes.settings.IInputAttributeSettings;
import seven.bsh.view.attributes.values.IDataValue;

public abstract class InputAttributeItem extends AttributeItem implements IInputAttribute {
    private BaseInputField mField;
    private IDataValue mValue;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public InputAttributeItem(Context context, IInputAttributeSettings settings, IDataValue value) {
        super(context, settings);
        value.setSettings(settings);
        mValue = value;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean validate() {
        boolean result = mValue.validate();
        mField.applyValidation();
        return result;
    }

    public void prepareDataValue() {
        mField.prepareDataValue();
    }

    @Override
    public View createFieldView() {
        initField();
        layout = mField.getView();
        return layout;
    }

    @Override
    public View createValueView() {
        initView();
        layout = getView().getView();
        return layout;
    }

    @Override
    public SerializableData serialize() {
        Object data = getValue().serialize();
        if (data == null) {
            return null;
        }
        return new SerializableData(getName(), data);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected abstract void initView();

    protected abstract void initField();

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    protected BaseInputField getField() {
        return mField;
    }

    protected void setField(BaseInputField field) {
        mField = field;
    }

    public String getName() {
        IInputAttributeSettings settings = (IInputAttributeSettings) getSettings();
        return settings.getName();
    }

    public IDataValue getValue() {
        return mValue;
    }

    @Override
    public void setData(Object data) {
        mValue.parse(data);
    }

    @Override
    public boolean isChanged() {
        return getValue().isChanged();
    }
}
