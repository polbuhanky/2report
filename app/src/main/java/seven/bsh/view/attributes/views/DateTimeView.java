package seven.bsh.view.attributes.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.settings.DateTimeSettings;
import seven.bsh.view.attributes.values.DateTimeValue;

public class DateTimeView extends BaseInputView {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public DateTimeView(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        DateTimeSettings settings = (DateTimeSettings) getSettings();
        DateTimeValue value = (DateTimeValue) getValue();
        View view;
        if (settings.hasTime() && settings.hasDate()) {
            view = View.inflate(getContext(), R.layout.partial_attribute_datetime_view, null);
        } else if (settings.hasDate()) {
            view = View.inflate(getContext(), R.layout.partial_attribute_date_view, null);
        } else {
            view = View.inflate(getContext(), R.layout.partial_attribute_time_view, null);
        }

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        if (settings.hasDate()) {
            TextView dateLabel = view.findViewById(R.id.date_choice_label);
            dateLabel.setText(value.getFormattedDate());
        }
        if (settings.hasTime()) {
            TextView timeLabel = view.findViewById(R.id.time_choice_label);
            timeLabel.setText(value.getFormattedTime());
        }
        return view;
    }
}
