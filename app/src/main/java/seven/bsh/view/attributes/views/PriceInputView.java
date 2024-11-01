package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.values.NumberInputValue;

public class PriceInputView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public PriceInputView(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        NumberInputValue value = (NumberInputValue) getValue();
        View view = View.inflate(getContext(), R.layout.partial_attribute_text_input_view, null);
        TextView viewField = view.findViewById(R.id.edit_field);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        String text = String.valueOf(value.getValue());
        if (text == null || text.isEmpty()) {
            viewField.setText(getString(R.string.attribute_text_empty));
        } else {
            viewField.setText(text);
        }
        return view;
    }
}
