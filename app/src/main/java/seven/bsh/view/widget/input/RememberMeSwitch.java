package seven.bsh.view.widget.input;

import android.annotation.TargetApi;
import android.content.Context;
import androidx.appcompat.widget.SwitchCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import seven.bsh.R;

public class RememberMeSwitch extends RelativeLayout {
    private SwitchCompat mSwitch;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public RememberMeSwitch(Context context) {
        super(context);
        init();
    }

    public RememberMeSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RememberMeSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public RememberMeSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init() {
        inflate(getContext(), R.layout.partial_switch_remember_me, this);
        mSwitch = findViewById(R.id.checkbox);
    }

    //---------------------------------------------------------------------------
    //
    // ACCESSORS
    //
    //---------------------------------------------------------------------------

    public boolean isChecked() {
        return mSwitch.isChecked();
    }

    public void setChecked(boolean checked) {
        mSwitch.setChecked(checked);
    }
}
