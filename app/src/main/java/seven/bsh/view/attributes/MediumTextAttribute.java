package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.R;

public class MediumTextAttribute extends BaseStaticTextAttribute {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MediumTextAttribute(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getViewResource() {
        return R.layout.partial_attribute_text;
    }
}
