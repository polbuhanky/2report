package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.NumberInputField;
import seven.bsh.view.attributes.settings.NumberInputSettings;
import seven.bsh.view.attributes.values.NumberInputValue;
import seven.bsh.view.attributes.views.NumberInputView;

public class NumberInputAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public NumberInputAttribute(Context context) {
        super(context, new NumberInputSettings(), new NumberInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new NumberInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new NumberInputView(getContext(), this));
    }
}
