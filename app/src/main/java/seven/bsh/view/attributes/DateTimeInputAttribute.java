package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.view.attributes.fields.DateTimeField;
import seven.bsh.view.attributes.settings.DateTimeSettings;
import seven.bsh.view.attributes.values.DateTimeValue;
import seven.bsh.view.attributes.views.DateTimeView;

public class DateTimeInputAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public DateTimeInputAttribute(Context context) {
        super(context, new DateTimeSettings(), new DateTimeValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public void initField() {
        setField(new DateTimeField(getContext(), this));
    }

    @Override
    public void initView() {
        setView(new DateTimeView(getContext(), this));
    }
}
