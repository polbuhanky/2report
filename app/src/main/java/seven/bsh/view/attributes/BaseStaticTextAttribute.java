package seven.bsh.view.attributes;

import android.content.Context;
import android.view.View;

import seven.bsh.view.attributes.settings.TextSettings;
import seven.bsh.view.attributes.views.StaticTextView;

public abstract class BaseStaticTextAttribute extends AttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseStaticTextAttribute(Context context) {
        super(context, new TextSettings());
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected abstract int getViewResource();

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
        setView(new StaticTextView(getContext(), this, getViewResource()));
        layout = getView().getView();
        return layout;
    }
}
