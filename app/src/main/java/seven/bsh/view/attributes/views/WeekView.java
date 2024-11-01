package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.joda.time.DateTime;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.WeekAttribute;
import seven.bsh.view.attributes.values.WeekInputValue;

public class WeekView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public WeekView(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        WeekInputValue value = (WeekInputValue) getValue();
        View view = View.inflate(getContext(), R.layout.partial_attribute_week_view, null);
        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        TextView week = view.findViewById(R.id.week_label);
        DateTime date = value.getDate();
        if (date == null) {
            week.setText(R.string.attribute_week_empty);
        } else {
            WeekAttribute attribute = (WeekAttribute) getAttribute();
            week.setText(attribute.getWeek(date));
        }
        return view;
    }
}
