package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.values.CheckboxValue;

public class CheckboxView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public CheckboxView(Context context, IInputAttribute attribute) {
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

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setText(getTitleLabel());
        checkBox.setChecked(value.isChecked());
        checkBox.setEnabled(false);
        return view;
    }
}
