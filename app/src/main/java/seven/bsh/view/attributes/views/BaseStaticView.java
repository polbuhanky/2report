package seven.bsh.view.attributes.views;

import android.content.Context;

import seven.bsh.view.attributes.IAttribute;
import seven.bsh.view.attributes.settings.IAttributeSettings;

public abstract class BaseStaticView implements IStaticView {
    private final Context context;
    private final IAttribute attribute;
    private final IAttributeSettings settings;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public BaseStaticView(Context context, IAttribute attribute) {
        this.context = context;
        this.attribute = attribute;
        settings = attribute.getSettings();
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public boolean hasValue() {
        return false;
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    public Context getContext() {
        return context;
    }

    protected IAttribute getAttribute() {
        return attribute;
    }

    public IAttributeSettings getSettings() {
        return settings;
    }
}
