package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.MultiSelectAttribute;
import seven.bsh.view.attributes.settings.MultiSelectSettings;
import seven.bsh.view.attributes.values.MultiSelectValue;
import seven.bsh.view.widget.spinner.MultiChoiceSpinner;

public class MultiSelectField extends BaseInputField {
    private MultiChoiceSpinner mSpinner;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MultiSelectField(Context context, MultiSelectAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        MultiSelectSettings settings = (MultiSelectSettings) getSettings();
        View view = View.inflate(getContext(), R.layout.partial_attribute_multiselect, null);
        errorField = view.findViewById(R.id.error);

        TextView textView = view.findViewById(R.id.label);
        textView.setText(getTitleLabel());

        mSpinner = view.findViewById(R.id.spinner);
        mSpinner.setList(settings.getItems());

        MultiSelectValue value = (MultiSelectValue) getValue();
        mSpinner.setSelectedIndexes(value.getIndexes());
        return view;
    }

    @Override
    public void prepareDataValue() {
        MultiSelectValue value = (MultiSelectValue) getValue();
        value.setIndexes(mSpinner.getSelectedIndexes());
    }
}
