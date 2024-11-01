package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.values.CheckboxValue;

public class CheckboxField extends BaseInputField {
    private CheckBox mCheckBox;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CheckboxField(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        CheckboxValue value = (CheckboxValue) getValue();
        View view = View.inflate(getContext(), R.layout.partial_attribute_checkbox, null);
        errorField = view.findViewById(R.id.error);

        mCheckBox = view.findViewById(R.id.checkbox);
        mCheckBox.setText(getTitleLabel());
        mCheckBox.setChecked(value.isChecked());
        return view;
    }

    public void prepareDataValue() {
        CheckboxValue value = (CheckboxValue) getValue();
        value.setChecked(mCheckBox.isChecked());
    }
}
