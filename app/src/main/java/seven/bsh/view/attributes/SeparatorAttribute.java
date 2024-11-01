package seven.bsh.view.attributes;

import android.content.Context;
import android.view.View;

import seven.bsh.R;

public class SeparatorAttribute extends AttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SeparatorAttribute(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View createFieldView() {
        return createValueView();
    }

    @Override
    public View createValueView() {
        return View.inflate(getContext(), R.layout.partial_attribute_separator, null);
    }
}
