package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.PriceInputAttribute;
import seven.bsh.view.attributes.fields.filters.PriceInputFilter;
import seven.bsh.view.attributes.values.NumberInputValue;

public class PriceInputField extends BaseInputField {
    private EditText inputField;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public PriceInputField(Context context, PriceInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        View view = View.inflate(getContext(), R.layout.partial_attribute_text_input, null);
        errorField = view.findViewById(R.id.error);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        inputField = view.findViewById(R.id.edit_field);
        inputField.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputField.setFilters(new InputFilter[]{
            new PriceInputFilter(),
        });

        NumberInputValue value = (NumberInputValue) getValue();
        inputField.setText(String.valueOf(value.getValue()));
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
