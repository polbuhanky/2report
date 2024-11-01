package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.SelectField;
import seven.bsh.view.attributes.settings.SelectSettings;
import seven.bsh.view.attributes.values.SelectValue;
import seven.bsh.view.attributes.views.SelectView;

public class SelectAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SelectAttribute(Context context) {
        super(context, new SelectSettings(context), new SelectValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new SelectField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new SelectView(getContext(), this));
    }
}
