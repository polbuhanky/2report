package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.SkuInputField;
import seven.bsh.view.attributes.settings.DefaultSettings;
import seven.bsh.view.attributes.values.SkuInputValue;
import seven.bsh.view.attributes.views.SkuView;

public class SkuAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SkuAttribute(Context context) {
        super(context, new DefaultSettings(), new SkuInputValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new SkuInputField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new SkuView(getContext(), this));
    }
}
