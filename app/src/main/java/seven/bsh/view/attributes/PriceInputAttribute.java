package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.PriceInputField;
import seven.bsh.view.attributes.settings.DefaultSettings;
import seven.bsh.view.attributes.values.PriceInputValue;
import seven.bsh.view.attributes.views.PriceInputView;

public class PriceInputAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public PriceInputAttribute(Context context) {
        super(context, new DefaultSettings(), new PriceInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new PriceInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new PriceInputView(getContext(), this));
    }
}
