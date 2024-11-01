package seven.bsh.view.widget.spinner;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import seven.bsh.R;

public class MultiChoiceSpinner extends SingleChoiceSpinner implements OnMultiChoiceClickListener {
    private boolean[] mSelectedItems;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public MultiChoiceSpinner(Context context) {
        super(context);
    }

    public MultiChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MultiChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(getContext())
            .setMultiChoiceItems(list, mSelectedItems, this)
            .setPositiveButton(getContext().getString(R.string.dialog_button_ok), this)
            .create()
            .show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        showSelectedItems();
        if (listener != null) {
            listener.onSpinnerChooseListener(this, -1);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        mSelectedItems[which] = isChecked;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void showSelectedItems() {
        boolean isEmpty = true;
        StringBuilder text = new StringBuilder();
        int length = mSelectedItems.length;

        for (int i = 0; i < length; i++) {
            if (mSelectedItems[i]) {
                isEmpty = false;
                text.append(list[i]).append(", ");
            }
        }

        if (isEmpty) {
            labelField.setText(getContext().getString(R.string.attribute_select_empty));
        } else {
            labelField.setText(text.toString());
        }
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public List<Integer> getSelectedIndexes() {
        List<Integer> checkedIndexes = new ArrayList<>();
        int length = mSelectedItems.length;
        for (int i = 0; i < length; i++) {
            if (mSelectedItems[i]) {
                checkedIndexes.add(i);
            }
        }
        return checkedIndexes;
    }

    public void setSelectedIndexes(List<Integer> indexes) {
        for (int index : indexes) {
            mSelectedItems[index] = true;
        }
        showSelectedItems();
    }

    @Override
    public void setList(String[] list) {
        this.list = list;
        mSelectedItems = new boolean[list.length];
        labelField.setText(getContext().getString(R.string.attribute_select_empty));
    }
}
