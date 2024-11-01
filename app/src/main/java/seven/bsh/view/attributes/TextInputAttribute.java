package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.TextInputField;
import seven.bsh.view.attributes.settings.TextInputSettings;
import seven.bsh.view.attributes.values.TextInputValue;
import seven.bsh.view.attributes.views.TextInputView;

public class TextInputAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TextInputAttribute(Context context) {
        super(context, new TextInputSettings(), new TextInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new TextInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new TextInputView(getContext(), this));
    }
}
