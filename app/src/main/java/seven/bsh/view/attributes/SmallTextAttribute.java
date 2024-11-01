package seven.bsh.view.attributes;

import android.content.Context;

import seven.bsh.R;

public class SmallTextAttribute extends BaseStaticTextAttribute {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SmallTextAttribute(Context context) {
        super(context);
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    protected int getViewResource() {
        return R.layout.partial_attribute_comment;
    }
}
