package seven.bsh.view.widget.spinner;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import seven.bsh.view.widget.dialog.SearchListViewDialog;

public class SearchSingleChoiceSpinner extends SingleChoiceSpinner {

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public SearchSingleChoiceSpinner(Context context) {
        super(context);
    }

    public SearchSingleChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchSingleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public SearchSingleChoiceSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        SearchListViewDialog dialog = new SearchListViewDialog(getContext());
        dialog.setListener(new SearchListViewDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                setSelectedIndex(index - 1);
            }
        });
        dialog.setList(list);
        dialog.show();
    }
}
