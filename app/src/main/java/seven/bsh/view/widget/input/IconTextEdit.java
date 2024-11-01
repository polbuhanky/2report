package seven.bsh.view.widget.input;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.view.IconicsTextView;

import seven.bsh.R;

public class IconTextEdit extends RelativeLayout implements
    View.OnFocusChangeListener,
    View.OnTouchListener {
    private EditText mInput;
    private IconicsTextView mIconLabel;
    private boolean mHasError;

    //---------------------------------------------------------------------------
    //
    // CONSTRUCTOR
    //
    //---------------------------------------------------------------------------

    public IconTextEdit(Context context) {
        super(context);
        init(null);
    }

    public IconTextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IconTextEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(21)
    public IconTextEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    //---------------------------------------------------------------------------
    //
    // HANDLERS
    //
    //---------------------------------------------------------------------------

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mIconLabel.setHeight(getMeasuredHeight());
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (mHasError) {
            return;
        }

        if (hasFocus) {
            setBackgroundResource(R.drawable.stroke_pressed_edit_text_light);
        } else {
            setBackgroundResource(R.drawable.stroke_normal_edit_text_light);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInput.setFocusableInTouchMode(true);
                break;

            case MotionEvent.ACTION_UP:
                v.performClick();
                break;
        }
        return false;
    }

    //---------------------------------------------------------------------------
    //
    // PRIVATE METHODS
    //
    //---------------------------------------------------------------------------

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.partial_edit_icon, this);
        mInput = findViewById(R.id.input);
        mIconLabel = findViewById(R.id.icon);

        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IconTextEdit, 0, 0);
            String hint = a.getString(R.styleable.IconTextEdit_tr_hint);
            String icon = a.getString(R.styleable.IconTextEdit_tr_faIcon);
            int inputType = a.getInt(R.styleable.IconTextEdit_android_inputType, InputType.TYPE_CLASS_TEXT);
            a.recycle();

            mInput.setHint(hint);
            mInput.setInputType(inputType);
            mInput.setOnFocusChangeListener(this);
            mInput.setOnTouchListener(this);

            mIconLabel.setText(icon);
        }
    }

    //---------------------------------------------------------------------------
    //
    // PUBLIC ACCESSORS
    //
    //---------------------------------------------------------------------------

    public String getText() {
        return mInput.getText().toString();
    }

    public void setText(String text) {
        mInput.setText(text);
    }

    public boolean hasError() {
        return mHasError;
    }

    public void setHasError(boolean hasError) {
        if (mHasError == hasError) {
            return;
        }

        mHasError = hasError;
        if (hasError) {
            setBackgroundResource(R.drawable.stroke_error_edit_text_light);
        } else if (mInput.isFocused()) {
            setBackgroundResource(R.drawable.stroke_pressed_edit_text_light);
        } else {
            setBackgroundResource(R.drawable.stroke_normal_edit_text_light);
        }
    }
}
