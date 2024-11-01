package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.SelectAttribute;
import seven.bsh.view.attributes.settings.SelectSettings;
import seven.bsh.view.attributes.values.SelectValue;

public class SelectView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SelectView(Context context, SelectAttribute attribute) {
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
        View view = View.inflate(getContext(), R.layout.partial_attribute_select_view, null);
        TextView viewField = view.findViewById(R.id.value);

        TextView textField = view.findViewById(R.id.label);
        textField.setText(getTitleLabel());

        int index = value.getIndex();
        if (index == -1) {
            viewField.setText(getString(R.string.attribute_select_empty));
        } else {
            String[] list = settings.getItems();
            viewField.setText(list[index + 1]);
        }

        return view;
    }
}
