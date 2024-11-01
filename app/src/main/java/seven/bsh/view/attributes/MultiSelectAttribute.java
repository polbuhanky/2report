package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.MultiSelectField;
import seven.bsh.view.attributes.settings.MultiSelectSettings;
import seven.bsh.view.attributes.values.MultiSelectValue;
import seven.bsh.view.attributes.views.MultiSelectView;

public class MultiSelectAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MultiSelectAttribute(Context context) {
        super(context, new MultiSelectSettings(), new MultiSelectValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new MultiSelectField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new MultiSelectView(getContext(), this));
    }
}
