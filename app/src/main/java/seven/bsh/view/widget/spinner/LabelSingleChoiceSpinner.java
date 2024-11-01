package seven.bsh.view.widget.spinner;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import seven.bsh.R;

public class LabelSingleChoiceSpinner extends RelativeLayout implements
    View.OnClickListener,
    DialogInterface.OnClickListener {
    protected String[] list;
    protected TextView labelField;
    protected TextView choiceBtn;

    private int mSelectedIndex = -1;
    private OnSpinnerChooseListener mListener;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public LabelSingleChoiceSpinner(Context context) {
        super(context);
        init();
    }

    public LabelSingleChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelSingleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public LabelSingleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(getContext())
            .setItems(list, this)
            .create()
            .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mSelectedIndex = (which == 0) ? -1 : which - 1;
        choiceBtn.setText(list[which]);
        if (mListener != null) {
            mListener.onSpinnerChooseListener(this, mSelectedIndex);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init() {
        inflate(getContext(), R.layout.partial_spinner, this);
        labelField = findViewById(R.id.label);
        choiceBtn = findViewById(R.id.choice_btn);
        setOnClickListener(this);
        setClickable(true);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public void setLabel(String label) {
        labelField.setText(label);
    }

    public void setList(String[] list) {
        this.list = list;
        choiceBtn.setText(list[0]);
    }

    public String getSelectedText() {
        return String.valueOf(choiceBtn.getText());
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int index) {
        mSelectedIndex = index;
        choiceBtn.setText(list[index]);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            choiceBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.accent));
        } else {
            choiceBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.semi_gray));
        }
    }

    public void setListener(OnSpinnerChooseListener listener) {
        mListener = listener;
    }

    //---------------------------------------------------------------------------
    //
    // INTERFACES
    //
    //---------------------------------------------------------------------------

    public interface OnSpinnerChooseListener {
        void onSpinnerChooseListener(LabelSingleChoiceSpinner target, int index);
    }
}
