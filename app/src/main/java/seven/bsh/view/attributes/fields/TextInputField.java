package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.TextInputAttribute;
import seven.bsh.view.attributes.settings.TextInputSettings;
import seven.bsh.view.attributes.values.TextInputValue;

public class TextInputField extends BaseInputField {
    protected EditText inputField;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public TextInputField(Context context, TextInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        TextInputSettings settings = (TextInputSettings) getSettings();
        View view;
        if (settings.isMultiline()) {
            view = View.inflate(getContext(), R.layout.partial_attribute_text_area, null);
        } else {
            view = View.inflate(getContext(), R.layout.partial_attribute_text_input, null);
        }

        errorField = view.findViewById(R.id.error);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        inputField = view.findViewById(R.id.edit_field);
        if (settings.getMax() != 0) {
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(settings.getMax());
            inputField.setFilters(filterArray);
        }

        TextInputValue value = (TextInputValue) getValue();
        inputField.setText(value.getValue());
        return view;
    }

    @Override
    public void prepareDataValue() {
        TextInputValue value = (TextInputValue) getValue();
        value.setValue(inputField.getText().toString().trim());
    }
}
