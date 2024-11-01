package seven.bsh.view.attributes;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import seven.bsh.view.attributes.fields.CheckboxField;
import seven.bsh.view.attributes.settings.CheckboxSettings;
import seven.bsh.view.attributes.values.CheckboxValue;
import seven.bsh.view.attributes.views.CheckboxView;

public class CheckBoxAttribute extends InputAttributeItem {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CheckBoxAttribute(Context context) {
        super(context, new CheckboxSettings(), new CheckboxValue(context));
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    protected void parseSettings(JSONObject data) throws JSONException {
        super.parseSettings(data);
        getValue().updateFromSettings();
    }

    protected void initField() {
        setField(new CheckboxField(getContext(), this));
    }

    protected void initView() {
        setView(new CheckboxView(getContext(), this));
    }
}
