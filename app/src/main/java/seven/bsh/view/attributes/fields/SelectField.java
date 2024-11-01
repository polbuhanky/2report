package seven.bsh.view.attributes.fields;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.SelectAttribute;
import seven.bsh.view.attributes.settings.SelectSettings;
import seven.bsh.view.attributes.values.SelectValue;
import seven.bsh.view.widget.spinner.SingleChoiceSpinner;

public class SelectField extends BaseInputField {
    private SingleChoiceSpinner mSpinner;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SelectField(Context context, SelectAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        SelectSettings settings = (SelectSettings) getSettings();
        SelectValue value = (SelectValue) getValue();

        View view = View.inflate(getContext(), R.layout.partial_attribute_select, null);
        errorField = view.findViewById(R.id.error);

        TextView textView = view.findViewById(R.id.label);
        textView.setText(getTitleLabel());

        mSpinner = view.findViewById(R.id.spinner);
        mSpinner.setList(settings.getItems());
        mSpinner.setSelectedIndex(value.getIndex());
        return view;
    }

    @Override
    public void prepareDataValue() {
        SelectValue value = (SelectValue) getValue();
        value.setIndex(mSpinner.getSelectedIndex());
    }
}
