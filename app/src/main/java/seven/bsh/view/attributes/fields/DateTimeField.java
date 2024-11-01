package seven.bsh.view.attributes.fields;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import seven.bsh.R;
import seven.bsh.view.attributes.IInputAttribute;
import seven.bsh.view.attributes.settings.DateTimeSettings;
import seven.bsh.view.attributes.values.DateTimeValue;

public class DateTimeField extends BaseInputField implements
    View.OnClickListener,
    TimePickerDialog.OnTimeSetListener,
    DatePickerDialog.OnDateSetListener,
    DialogInterface.OnCancelListener {
    private TextView mTimeLabel;
    private TextView mDateLabel;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public DateTimeField(Context context, IInputAttribute attribute) {
        super(context, attribute);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view == mTimeLabel) {
            createTimePickerDialog();
        } else {
            createDatePickerDialog();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DateTimeValue value = (DateTimeValue) getValue();
        String hours = getFormattedComponent(String.valueOf(hourOfDay));
        String minutes = getFormattedComponent(String.valueOf(minute));
        value.setTime(hours + ":" + minutes + ":" + "00");
        mTimeLabel.setText(hours + ":" + minutes);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        DateTimeValue value = (DateTimeValue) getValue();
        String month = getFormattedComponent(String.valueOf(monthOfYear + 1));
        String day = getFormattedComponent(String.valueOf(dayOfMonth));
        value.setDate(year + "-" + month + "-" + day);
        mDateLabel.setText(day + "." + month + "." + year);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if (dialogInterface instanceof DatePickerDialog) {
            mDateLabel.setText(R.string.attribute_datetime_date);
        } else {
            mTimeLabel.setText(R.string.attribute_datetime_time);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void createTimePickerDialog() {
        Context context = getContext();
        String[] time = mTimeLabel.getText().toString().split(":");
        TimePickerDialog dialog;
        if (time.length == 2) {
            dialog = new TimePickerDialog(
                context,
                this,
                Integer.valueOf(time[0]),
                Integer.valueOf(time[1]),
                DateFormat.is24HourFormat(context)
            );
        } else {
            Calendar now = Calendar.getInstance();
            dialog = new TimePickerDialog(
                context,
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)
            );
        }
        dialog.setOnCancelListener(this);
        dialog.setTitle(null);
        dialog.show();
    }

    private void createDatePickerDialog() {
        Context context = getContext();
        String[] date = mDateLabel.getText().toString().split("\\.");
        DatePickerDialog dialog;
        if (date.length == 3) {
            dialog = new DatePickerDialog(
                context,
                this,
                Integer.valueOf(date[2]),
                Integer.valueOf(date[1]) - 1,
                Integer.valueOf(date[0])
            );
        } else {
            Calendar now = Calendar.getInstance();
            dialog = new DatePickerDialog(
                context,
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            );
        }
        dialog.setOnCancelListener(this);
        dialog.setTitle(null);
        dialog.show();
    }

    private String getFormattedComponent(String source) {
        if (source.length() == 1) {
            return "0" + source;
        }
        return source;
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
        Context context = getContext();
        View view;
        if (settings.hasTime() && settings.hasDate()) {
            view = View.inflate(context, R.layout.partial_attribute_datetime, null);
        } else if (settings.hasDate()) {
            view = View.inflate(context, R.layout.partial_attribute_date, null);
        } else {
            view = View.inflate(context, R.layout.partial_attribute_time, null);
        }

        errorField = view.findViewById(R.id.error);
        TextView labelField = view.findViewById(R.id.label);
        labelField.setText(getTitleLabel());

        if (settings.hasDate()) {
            mDateLabel = view.findViewById(R.id.date_choice_label);
            mDateLabel.setOnClickListener(this);

            String date = value.getFormattedDate();
            if (!date.isEmpty()) {
                mDateLabel.setText(value.getFormattedDate());
            } else {
                mDateLabel.setText(R.string.attribute_datetime_date);
            }
        }
        if (settings.hasTime()) {
            mTimeLabel = view.findViewById(R.id.time_choice_label);
            mTimeLabel.setOnClickListener(this);

            String time = value.getFormattedTime();
            if (!time.isEmpty()) {
                mTimeLabel.setText(value.getFormattedTime());
            } else {
                mTimeLabel.setText(R.string.attribute_datetime_time);
            }
        }
        return view;
    }

    @Override
    public void prepareDataValue() {
        // ignored
    }
}
