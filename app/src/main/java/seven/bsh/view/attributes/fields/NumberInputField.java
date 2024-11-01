package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.NumberInputAttribute;
import seven.bsh.view.attributes.settings.NumberInputSettings;
import seven.bsh.view.attributes.values.NumberInputValue;

public class NumberInputField extends BaseInputField {
    private EditText inputField;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public NumberInputField(Context context, NumberInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        NumberInputSettings settings = (NumberInputSettings) getSettings();
        View view = View.inflate(getContext(), R.layout.partial_attribute_text_input, null);
        errorField = view.findViewById(R.id.error);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        inputField = view.findViewById(R.id.edit_field);
        if (settings.isFloat()) {
            inputField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else {
            inputField.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        NumberInputValue value = (NumberInputValue) getValue();
        inputField.setText(value.getValue());
        return view;
    }

    @Override
    public void prepareDataValue() {
        NumberInputValue value = (NumberInputValue) getValue();
        value.setValue(getInputValue());
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getInputValue() {
        return inputField.getText().toString().trim();
    }
}
