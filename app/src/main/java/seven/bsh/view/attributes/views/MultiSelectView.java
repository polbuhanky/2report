package seven.bsh.view.attributes.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import seven.bsh.R;
import seven.bsh.view.attributes.MultiSelectAttribute;
import seven.bsh.view.attributes.settings.MultiSelectSettings;
import seven.bsh.view.attributes.values.MultiSelectValue;

public class MultiSelectView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MultiSelectView(Context context, MultiSelectAttribute attribute) {
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
        MultiSelectValue value = (MultiSelectValue) getValue();
        View view = View.inflate(getContext(), R.layout.partial_attribute_select_view, null);
        TextView viewField = view.findViewById(R.id.value);

        TextView textField = view.findViewById(R.id.label);
        textField.setText(getTitleLabel());

        List<Integer> indexes = value.getIndexes();
        if (indexes.isEmpty()) {
            viewField.setText(getString(R.string.attribute_select_empty));
        } else {
            String[] list = settings.getItems();
            String[] temp = new String[indexes.size()];
            for (int i = 0; i < indexes.size(); i++) {
                temp[i] = list[indexes.get(i)];
            }
            viewField.setText(TextUtils.join(", ", temp));
        }

        return view;
    }
}
