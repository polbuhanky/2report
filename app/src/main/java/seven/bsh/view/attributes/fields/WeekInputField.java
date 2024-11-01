package seven.bsh.view.attributes.fields;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.DateTime;

import seven.bsh.R;
import seven.bsh.view.attributes.WeekAttribute;
import seven.bsh.view.attributes.values.WeekInputValue;

public class WeekInputField extends BaseInputField implements
    View.OnClickListener,
    DatePickerDialog.OnDateSetListener,
    DialogInterface.OnCancelListener {
    private TextView mWeekLabel;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public WeekInputField(Context context, WeekAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        Context context = getContext();
        DatePickerDialog dialog;
        WeekInputValue value = (WeekInputValue) getValue();
        DateTime date = value.getDate();
        if (date == null) {
            date = DateTime.now();
        }

        dialog = new DatePickerDialog(
            context,
            this,
            date.getYear(),
            date.getMonthOfYear() - 1,
            date.getDayOfMonth()
        );
        dialog.setOnCancelListener(this);
        dialog.setTitle(null);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        WeekAttribute attribute = (WeekAttribute) getAttribute();
        WeekInputValue value = (WeekInputValue) getValue();
        DateTime date = (new DateTime()).withDate(year, monthOfYear + 1, dayOfMonth);

        int dayOfWeek = date.getDayOfWeek() - 1;
        if (dayOfWeek > 0) {
            date = date.minusDays(dayOfWeek);
        }

        value.setDate(date);
        mWeekLabel.setText(attribute.getWeek(date));
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mWeekLabel.setText(R.string.attribute_week_choice);
        WeekInputValue value = (WeekInputValue) getValue();
        value.setDate(null);
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC METHODS
    //
    //---------------------------------------------------------------------------

    @Override
    public View getView() {
        View view = View.inflate(getContext(), R.layout.partial_attribute_week, null);
        errorField = view.findViewById(R.id.error);

        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        mWeekLabel = view.findViewById(R.id.date_choice_label);
        mWeekLabel.setOnClickListener(this);

        WeekAttribute attribute = (WeekAttribute) getAttribute();
        WeekInputValue value = (WeekInputValue) getValue();
        if (value.getDate() != null) {
            mWeekLabel.setText(attribute.getWeek(value.getDate()));
        }
        return view;
    }

    @Override
    public void prepareDataValue() {
        // ignored
    }
}
